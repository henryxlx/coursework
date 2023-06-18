package org.edunext.coursework.kernel.service;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.*;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppLogService;
import com.jetwinner.webfast.kernel.service.AppNotificationService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.dao.CourseThreadDao;
import org.edunext.coursework.kernel.dao.CourseThreadPostDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    private final AppLogService logService;

    public CourseThreadServiceImpl(CourseThreadDao threadDao,
                                   CourseThreadPostDao threadPostDao,
                                   CourseService courseService,
                                   AppNotificationService notificationService,
                                   AppUserService userService, AppLogService logService) {

        this.threadDao = threadDao;
        this.threadPostDao = threadPostDao;
        this.courseService = courseService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.logService = logService;
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
    public List<Map<String, Object>> findThreadElitePosts(Integer courseId, Integer threadId, int start, int limit) {
        return this.threadPostDao.findPostsByThreadIdAndIsElite(threadId, 1, start, limit);
    }

    @Override
    public void hitThread(Integer courseId, Integer threadId) {
        this.threadDao.waveThread(threadId, "hitNum", +1);
    }

    @Override
    public Integer getThreadPostCountByThreadId(Integer threadId) {
        return this.threadPostDao.getPostCountByThreadId(threadId);
    }

    @Override
    public Integer getPostCountByUserIdAndThreadId(Integer userId, Integer threadId) {
        return this.threadPostDao.getPostCountByUserIdAndThreadId(userId, threadId);
    }

    @Override
    public Map<String, Object> createPost(Map<String, Object> post, AppUser currentUser) {
        String[] requiredKeys = {"courseId", "threadId", "content"};
        if (!ArrayToolkit.required(post, requiredKeys)) {
            throw new RuntimeGoingException("参数缺失");
        }

        Integer postCourseId = ValueParser.toInteger(post.get("courseId"));
        Map<String, Object> thread = this.getThread(postCourseId,
                ValueParser.toInteger(post.get("threadId")));
        if (MapUtil.isEmpty(thread)) {
            throw new RuntimeGoingException(String.format("课程(ID: %s)话题(ID: %s)不存在。",
                    post.get("courseId"), post.get("threadId")));
        }

        Map<String, Object> member = this.courseService.tryTakeCourse(postCourseId, currentUser);

        post.put("userId", currentUser.getId());
        post.put("isElite", this.courseService.isCourseTeacher(postCourseId,
                ValueParser.toInteger(post.get("userId"))) ? 1 : 0);
        post.put("createdTime", System.currentTimeMillis());

        //创建post过滤html
        post.put("content", EasyStringUtil.purifyHtml(post.get("content")));
        post = this.threadPostDao.addPost(post);

        // 高并发的时候， 这样更新postNum是有问题的，这里暂时不考虑这个问题。
        Map<String, Object> threadFields = new HashMap<>(3);
        threadFields.put("postNum", ValueParser.parseInt(thread.get("postNum")) + 1);
        threadFields.put("latestPostUserId", post.get("userId"));
        threadFields.put("latestPostTime", post.get("createdTime"));
        this.threadDao.updateThread(thread.get("id"), threadFields);

        return post;
    }

    @Override
    public void deleteThread(Integer threadId, AppUser currentUser) {
        Map<String, Object> thread = this.threadDao.getThread(threadId);
        if (MapUtil.isEmpty(thread)) {
            throw new RuntimeGoingException("话题(ID: " + threadId + "不存在。");
        }

        if (!this.courseService.canManageCourse(ValueParser.toInteger(thread.get("courseId")), currentUser.getId())) {
            throw new RuntimeGoingException("您无权限删除该话题");
        }

        this.threadPostDao.deletePostsByThreadId(threadId);
        this.threadDao.deleteThread(threadId);

        this.logService.info(currentUser, "thread", "delete",
                String.format("删除话题 %s(%s)", thread.get("title"), thread.get("id")));
    }

    @Override
    public Map<String, Object> getPost(Integer courseId, Integer id) {
        Map<String, Object> post = this.threadPostDao.getPost(id);
        if (MapUtil.isEmpty(post) || ValueParser.parseInt(post.get("courseId")) != courseId) {
            return null;
        }
        return post;
    }

    @Override
    public void deletePost(Integer courseId, Integer id, AppUser currentUser) {
        Map<String, Object> course = this.courseService.tryManageCourse(currentUser, courseId);

        Map<String, Object> post = this.threadPostDao.getPost(id);
        if (MapUtil.isEmpty(post)) {
            throw new RuntimeGoingException("帖子(#" + id + ")不存在，删除失败。");
        }

        if (ValueParser.parseInt(post.get("courseId")) != courseId) {
            throw new RuntimeGoingException(String.format("帖子#%s不属于课程#%s，删除失败。", id, courseId));
        }

        this.threadPostDao.deletePost(post.get("id"));
        this.threadDao.waveThread(ValueParser.toInteger(post.get("threadId")), "postNum", -1);
    }

    @Override
    public void stickThread(Integer courseId, Integer threadId) {
        this.threadDao.updateThread(threadId, FastHashMap.build(1).add("isStick", 1).toMap());
    }

    @Override
    public void unstickThread(Integer courseId, Integer threadId) {
        this.threadDao.updateThread(threadId, FastHashMap.build(1).add("isStick", 0).toMap());
    }

    @Override
    public void eliteThread(Integer courseId, Integer threadId) {
        this.threadDao.updateThread(threadId, FastHashMap.build(1).add("isElite", 1).toMap());
    }

    @Override
    public void uneliteThread(Integer courseId, Integer threadId) {
        this.threadDao.updateThread(threadId, FastHashMap.build(1).add("isElite", 1).toMap());
    }

    @Override
    public Map<String, Object> updateThread(Object courseId, Object threadId, Map<String, Object> fields) {
        Map<String, Object> thread = this.getThread(ValueParser.toInteger(courseId), ValueParser.toInteger(threadId));
        if (MapUtil.isEmpty(thread)) {
            throw new RuntimeGoingException("话题不存在，更新失败！");
        }

        fields = ArrayToolkit.part(fields, "title", "content");
        if (MapUtil.isEmpty(fields)) {
            throw new RuntimeGoingException("参数缺失，更新失败。");
        }

        //更新thread过滤html
        fields.put("content", EasyStringUtil.purifyHtml(fields.get("content")));
        this.threadDao.updateThread(threadId, fields);
        return getThread(ValueParser.toInteger(courseId), ValueParser.toInteger(threadId));
    }

    @Override
    public Map<String, Object> updatePost(Object courseId, Object postId, Map<String, Object> fields) {
        Map<String, Object> post = this.getPost(ValueParser.toInteger(courseId), ValueParser.toInteger(postId));
        if (MapUtil.isEmpty(post)) {
            throw new RuntimeGoingException("回帖#{$id}不存在。");
        }

        fields = ArrayToolkit.part(fields, "content");
        if (MapUtil.isEmpty(fields)) {
            throw new RuntimeGoingException("参数缺失。");
        }

        //更新post过滤html
        fields.put("content", EasyStringUtil.purifyHtml(fields.get("content")));
        return this.threadPostDao.updatePost(postId, fields);
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
