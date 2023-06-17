package org.edunext.coursework.kernel.service;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppLogService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.dao.CourseNoteDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class CourseNoteServiceImpl implements CourseNoteService {

    private final CourseNoteDao noteDao;
    private final CourseService courseService;
    private final AppUserService userService;
    private final AppLogService logService;

    public CourseNoteServiceImpl(CourseNoteDao noteDao,
                                 CourseService courseService,
                                 AppUserService userService,
                                 AppLogService logService) {

        this.noteDao = noteDao;
        this.courseService = courseService;
        this.userService = userService;
        this.logService = logService;
    }

    @Override
    public Map<String, Object> getUserLessonNote(Integer userId, Integer lessonId) {
        return this.noteDao.getNoteByUserIdAndLessonId(userId, lessonId);
    }

    /**
     * 类似这样的，提交数据保存到数据的流程是：
     * 1. 检查参数是否正确，不正确就抛出异常
     * 2. 过滤数据
     * 3. 插入到数据库
     * 4. 更新其他相关的缓存字段
     *
     * @param note
     */
    @Override
    public void saveNote(Map<String, Object> note, AppUser currentUser) {
        if (!ArrayToolkit.required(note, "lessonId", "courseId", "content")) {
            throw new RuntimeGoingException("缺少必要的字段，保存笔记失败");
        }

        if (MapUtil.isEmpty(this.courseService.getCourseLesson(ValueParser.toInteger(note.get("courseId")),
                ValueParser.toInteger(note.get("lessonId"))))) {
            throw new RuntimeGoingException("课时不存在，保存笔记失败");
        }

        ArrayToolkit.filter(note, new ParamMap().add("courseId", 0).add("lessonId", 0).add("content", "").toMap());

        note.put("content", EasyStringUtil.purifyHtml(note.get("content")));
        note.put("length", this.calculateContentLength(note.get("content")));

        Map<String, Object> existNote = this.getUserLessonNote(currentUser.getId(),
                ValueParser.toInteger(note.get("lessonId")));
        if (MapUtil.isEmpty(existNote)) {
            note.put("userId", currentUser.getId());
            note.put("createdTime", System.currentTimeMillis());
            this.noteDao.addNote(note);
        } else {
            note.put("updatedTime", System.currentTimeMillis());
            this.noteDao.updateNote(existNote.get("id"), note);
        }

        Integer noteCourseId = ValueParser.toInteger(note.get("courseId"));
        Integer noteUserId = ValueParser.toInteger(note.get("userId"));
        this.courseService.setMemberNoteNumber(noteCourseId, noteUserId,
                this.noteDao.getNoteCountByUserIdAndCourseId(noteUserId, noteCourseId));
    }

    @Override
    public Integer searchNoteCount(Map<String, Object> conditions) {
        conditions = this.prepareSearchNoteConditions(conditions);
        return this.noteDao.searchNoteCount(conditions);
    }

    @Override
    public List<Map<String, Object>> searchNotes(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        OrderBy orderBy;
        switch (sort) {
            case "created":
                orderBy = OrderBy.build(1).addDesc("createdTime");
                break;
            case "updated":
                orderBy = OrderBy.build(1).addDesc("updatedTime");
                break;
            default:
                throw new RuntimeGoingException("参数sort不正确。");
        }

        conditions = this.prepareSearchNoteConditions(conditions);
        return this.noteDao.searchNotes(conditions, orderBy, start, limit);
    }

    @Override
    public void deleteNote(Integer id, AppUser currentUser) {
        Map<String, Object> note = this.getNote(id);
        if (MapUtil.isEmpty(note)) {
            throw new RuntimeGoingException("笔记(#" + id + ")不存在，删除失败");
        }

        int noteUserId = ValueParser.parseInt(note.get("userId"));
        if ((noteUserId != currentUser.getId()) &&
                !this.courseService.canManageCourse(ValueParser.toInteger(note.get("courseId")), currentUser.getId())) {

            throw new RuntimeGoingException("你没有权限删除笔记(#" + id + ")");
        }

        this.noteDao.deleteNote(id);

        Integer noteCourseId = ValueParser.toInteger(note.get("courseId"));
        this.courseService.setMemberNoteNumber(noteCourseId, noteUserId,
                this.noteDao.getNoteCountByUserIdAndCourseId(noteUserId, noteCourseId));

        if (noteUserId != currentUser.getId()) {
            this.logService.info(currentUser, "note", "delete", "删除笔记#" + id);
        }
    }

    @Override
    public void deleteNotes(String[] ids, AppUser currentUser) {
        for (String id : ids) {
            this.deleteNote(ValueParser.toInteger(id), currentUser);
        }
    }

    @Override
    public Map<String, Object> getNote(Object id) {
        return this.noteDao.getNote(id);
    }

    @Override
    public List<Map<String, Object>> findUserCourseNotes(Integer userId, Integer courseId) {
        return this.noteDao.findNotesByUserIdAndCourseId(userId, courseId);
    }

    private Integer calculateContentLength(Object objContent) {
        return objContent == null ? 0 : String.valueOf(objContent).length();
    }

    private Map<String, Object> prepareSearchNoteConditions(Map<String, Object> conditions) {

        if (EasyStringUtil.isNotBlank(conditions.get("keywordType")) &&
                EasyStringUtil.isNotBlank(conditions.get("keyword"))) {

            if (!ArrayUtil.inArray(conditions.get("keywordType"), "content", "courseId", "courseTitle")) {
                throw new RuntimeGoingException("keywordType参数不正确");
            }
            conditions.put(String.valueOf(conditions.get("keywordType")), conditions.get("keyword"));
        }
        conditions.remove("keywordType");
        conditions.remove("keyword");

        if (EasyStringUtil.isNotBlank(conditions.get("author"))) {
            AppUser author = this.userService.getUserByUsername(String.valueOf(conditions.get("author")));
            conditions.put("userId", author != null ? author.getId() : -1);
            conditions.remove("author");
        }

        return conditions;
    }
}
