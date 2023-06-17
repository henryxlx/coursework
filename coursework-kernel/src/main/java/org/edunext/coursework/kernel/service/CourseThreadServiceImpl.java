package org.edunext.coursework.kernel.service;

import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppNotificationService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.dao.CourseThreadDao;
import org.edunext.coursework.kernel.dao.CourseThreadPostDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class CourseThreadServiceImpl implements CourseThreadService {

    private final CourseThreadDao threadDao;
    private final CourseThreadPostDao threadPostDao;
    private final CourseService courseService;
    private final AppNotificationService notificationService;
    private final AppUserService userService;

    public CourseThreadServiceImpl(CourseThreadDao threadDao,
                                   CourseThreadPostDao threadPostDao,
                                   CourseService courseService,
                                   AppNotificationService notificationService,
                                   AppUserService userService) {

        this.threadDao = threadDao;
        this.threadPostDao = threadPostDao;
        this.courseService = courseService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @Override
    public int searchThreadCount(Map<String, Object> conditions) {
        conditions = this.prepareThreadSearchConditions(conditions);
        return this.threadDao.searchThreadCount(conditions);
    }

    @Override
    public List<Map<String, Object>> searchThreads(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        OrderBy orderBys = this.filterSort(sort);
        conditions = this.prepareThreadSearchConditions(conditions);
        return this.threadDao.searchThreads(conditions, orderBys, start, limit);
    }

    @Override
    public Map<String, Object> createThread(Map<String, Object> thread, AppUser currentUser) {
        if (EasyStringUtil.isBlank(thread.get("courseId"))) {
            throw new RuntimeGoingException("Course ID can not be empty.");
        }
        if (EasyStringUtil.isBlank(thread.get("type")) || !ArrayUtil.inArray(thread.get("type"), "discussion", "question")) {
            throw new RuntimeGoingException(String.format("Thread type(%s) is error.", thread.get("type")));
        }

        Integer threadCourseId = ValueParser.toInteger(thread.get("courseId"));
        Map<String, Object> member = this.courseService.tryTakeCourse(threadCourseId, currentUser);
        Map<String, Object> course = this.courseService.getCourse(threadCourseId);

        thread.put("userId", currentUser.getId());
        thread.put("title", EasyStringUtil.purifyHtml(thread.get("title")));

        //创建thread过滤html
        thread.put("content", EasyStringUtil.purifyHtml(thread.get("content")));
        thread.put("createdTime", System.currentTimeMillis());
        thread.put("latestPostUserId", thread.get("userId"));
        thread.put("latestPostTime", thread.get("createdTime"));
        thread.put("private", "published".equals(course.get("status")) ? 0 : 1);
        thread = this.threadDao.addThread(thread);

        if (course.get("teacherIds") instanceof String[]) {
            String[] teacherIds = (String[]) course.get("teacherIds");
            for (String teacherId : teacherIds) {
                if (ValueParser.parseInt(teacherId) == ValueParser.parseInt(thread.get("userId"))) {
                    continue;
                }

                if (!"question".equals(thread.get("type"))) {
                    continue;
                }

                this.notificationService.notify(ValueParser.toInteger(teacherId), "thread",
                        new ParamMap().add("threadId", thread.get("id"))
                                .add("threadUserId", thread.get("userId"))
                                .add("threadUserNickname", currentUser.getUsername())
                                .add("threadTitle", thread.get("title"))
                                .add("threadType", thread.get("type"))
                                .add("courseId", course.get("id"))
                                .add("courseTitle", course.get("title")).toMap());
            }
        }

        return thread;
    }

    @Override
    public int searchThreadCountInCourseIds(Map<String, Object> conditions) {
        conditions = this.prepareThreadSearchConditions(conditions);
        return this.threadDao.searchThreadCountInCourseIds(conditions);
    }

    @Override
    public List<Map<String, Object>> searchThreadInCourseIds(Map<String, Object> conditions, String sort,
                                                             Integer start, Integer limit) {

        OrderBy orderBy = this.filterSort(sort);
        conditions = this.prepareThreadSearchConditions(conditions);
        return this.threadDao.searchThreadInCourseIds(conditions, orderBy, start, limit);
    }

    @Override
    public Map<String, Object> getThread(Integer courseId, Integer threadId) {
        Map<String, Object> thread = this.threadDao.getThread(threadId);
        if (MapUtil.isEmpty(thread)) {
            return null;
        }
        return ValueParser.parseInt(thread.get("courseId")) == courseId ? thread : null;
    }

    @Override
    public Integer getThreadPostCount(Integer courseId, Integer threadId) {
        return this.threadPostDao.getPostCountByThreadId(threadId);
    }

    @Override
    public List<Map<String, Object>> findThreadPosts(Integer courseId, Integer threadId, String sort, Integer start, Integer limit) {
        Map<String, Object> thread = this.getThread(courseId, threadId);
        if (MapUtil.isEmpty(thread)) {
            return null;
        }
        OrderBy orderBy;
        if ("best".equals(sort)) {
            orderBy = OrderBy.build(1).addDesc("score");
        } else if ("elite".equals(sort)) {
            orderBy = OrderBy.build(2).addDesc("createdTime").addAsc("isElite");
        } else {
            orderBy = OrderBy.build(1).add("createdTime");
        }

        return this.threadPostDao.findPostsByThreadId(threadId, orderBy, start, limit);
    }

    @Override
    public Map<String, Object> findThreadElitePosts(Integer courseId, Integer threadId, int start, int limit) {
        return this.threadPostDao.findPostsByThreadIdAndIsElite(threadId, 1, start, limit);
    }

    @Override
    public void hitThread(Integer courseId, Integer threadId) {
        this.threadDao.waveThread(threadId, "hitNum", +1);
    }

    private OrderBy filterSort(String sort) {
        OrderBy orderBy;
        switch (sort) {
            case "created":
                orderBy = OrderBy.build(2).addDesc("isStick").addDesc("createdTime");
                break;
            case "posted":
                orderBy = OrderBy.build(2).addDesc("isStick").addDesc("latestPostTime");
                break;
            case "createdNotStick":
                orderBy = OrderBy.build(1).addDesc("createdTime");
                break;
            case "postedNotStick":
                orderBy = OrderBy.build(1).addDesc("latestPostTime");
                break;
            case "popular":
                orderBy = OrderBy.build(1).addDesc("hitNum");
                break;

            default:
                throw new RuntimeGoingException("参数sort不正确。");
        }
        return orderBy;
    }

    private Map<String, Object> prepareThreadSearchConditions(Map<String, Object> conditions) {

        if (EasyStringUtil.isBlank(conditions.get("type"))) {
            conditions.remove("type");
        }

        if (EasyStringUtil.isBlank(conditions.get("keyword"))) {
            conditions.remove("keyword");
            conditions.remove("keywordType");
        }

        if (EasyStringUtil.isBlank(conditions.get("threadType"))) {
            conditions.remove("threadType");
        }

        if (EasyStringUtil.isNotBlank(conditions.get("threadType"))) {
            conditions.put(String.valueOf(conditions.get("threadType")), 1);
            conditions.remove("threadType");
        }

        if (EasyStringUtil.isNotBlank(conditions.get("keywordType")) &&
                EasyStringUtil.isNotBlank(conditions.get("keyword"))) {

            if (!ArrayUtil.inArray(conditions.get("keywordType"),
                    "title", "content", "courseId", "courseTitle")) {

                throw new RuntimeGoingException("keywordType参数不正确");
            }
            conditions.put(String.valueOf(conditions.get("keywordType")), conditions.get("keyword"));
            conditions.remove("keywordType");
            conditions.remove("keyword");
        }

        if (EasyStringUtil.isBlank(conditions.get("author"))) {
            conditions.remove("author");
        }

        if (EasyStringUtil.isNotBlank(conditions.get("author"))) {
            AppUser author = this.userService.getUserByUsername(String.valueOf(conditions.get("author")));
            conditions.put("userId", author != null ? author.getId() : -1);
        }

        return conditions;
    }
}
