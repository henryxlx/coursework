package org.edunext.coursework.kernel.service;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.toolbag.MapKitOnJava8;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppLogService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import com.jetwinner.webfast.module.bigapp.service.AppTagService;
import org.edunext.coursework.kernel.dao.CourseDao;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xulixin
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final AppUserService userService;
    private final UserAccessControlService userAccessControlService;
    private final AppCategoryService categoryService;
    private final CourseDao courseDao;
    private final AppTagService tagService;
    private final AppLogService logService;

    public CourseServiceImpl(AppUserService userService,
                             UserAccessControlService userAccessControlService,
                             AppCategoryService categoryService,
                             CourseDao courseDao,
                             AppTagService tagService,
                             AppLogService logService) {

        this.userService = userService;
        this.userAccessControlService = userAccessControlService;
        this.categoryService = categoryService;
        this.courseDao = courseDao;
        this.tagService = tagService;
        this.logService = logService;
    }

    @Override
    public int searchCourseCount(Map<String, Object> conditions) {
        prepareCourseConditions(conditions);
        return courseDao.searchCourseCount(conditions);
    }

    private void prepareCourseConditions(Map<String, Object> conditions) {
        MapKitOnJava8.filter(conditions);

        /*if (Objects.nonNull(conditions.get("date"))) {
            Map<String, Map<String, Object>> dates =
            $dates = array(
                    'yesterday'=>array(
                    strtotime('yesterday'),
                    strtotime('today'),
                    ),
                    'today'=>array(
                    strtotime('today'),
                    strtotime('tomorrow'),
                    ),
                    'this_week' => array(
                    strtotime('Monday this week'),
                    strtotime('Monday next week'),
                    ),
                    'last_week' => array(
                    strtotime('Monday last week'),
                    strtotime('Monday this week'),
                    ),
                    'next_week' => array(
                    strtotime('Monday next week'),
                    strtotime('Monday next week', strtotime('Monday next week')),
                    ),
                    'this_month' => array(
                    strtotime('first day of this month midnight'),
                    strtotime('first day of next month midnight'),
                    ),
                    'last_month' => array(
                    strtotime('first day of last month midnight'),
                    strtotime('first day of this month midnight'),
                    ),
                    'next_month' => array(
                    strtotime('first day of next month midnight'),
                    strtotime('first day of next month midnight', strtotime('first day of next month midnight')),
                    ),
			);

            if (array_key_exists($conditions['date'], $dates)) {
                $conditions['startTimeGreaterThan'] = $dates[$conditions['date']][0];
                $conditions['startTimeLessThan'] = $dates[$conditions['date']][1];
                unset($conditions['date']);
            }
        }*/

        if (Objects.nonNull(conditions.get("creator"))) {
            AppUser user = userService.getUserByUsername(String.valueOf(conditions.get("creator")));
            conditions.put("userId", user != null ? user.getId() : -1);
            conditions.remove("creator");
        }

        if (Objects.nonNull(conditions.get("categoryId"))) {
            Set<Object> childrenIds = categoryService.findCategoryChildrenIds(conditions.get("categoryId"));
            childrenIds.add(conditions.get("categoryId"));
            conditions.put("categoryIds", childrenIds);
            conditions.remove("categoryId");
        }

        if (Objects.nonNull(conditions.get("username"))) {
            AppUser user = userService.getUserByUsername(String.valueOf(conditions.get("username")));
            conditions.put("userId", user != null ? user.getId() : -1);
            conditions.remove("username");
        }
    }

    @Override
    public List<Map<String, Object>> searchCourses(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        prepareCourseConditions(conditions);
        OrderBy orderBy = OrderBy.build(0);
        if ("popular".equals(sort)) {
            orderBy.addDesc("hitNum");
        } else if ("recommended".equals(sort)) {
            orderBy.addDesc("recommendedTime");
        } else if ("Rating".equals(sort)) {
            orderBy.addDesc("Rating");
        } else if ("hitNum".equals(sort)) {
            orderBy.addDesc("hitNum");
        } else if ("studentNum".equals(sort)) {
            orderBy.addDesc("studentNum");
        } else if ("recommendedSeq".equals(sort)) {
            orderBy.add("recommendedSeq");
        } else if ("createdTimeByAsc".equals(sort)) {
            orderBy.add("createdTime");
        } else {
            orderBy.addDesc("createdTime");
        }

        return courseDao.searchCourses(conditions, orderBy, start, limit);
    }

    @Override
    public Map<String, Object> createCourse(AppUser currentUser, Map<String, Object> fields) {
        if (!ArrayToolkit.required(fields, "title")) {
            throw new RuntimeGoingException("缺少必要字段，创建课程失败！");
        }

        Map<String, Object> course = ArrayToolkit.part(fields, "title", "type", "about", "categoryId",
                "tags", "price", "startTime", "endTime", "locationId", "address");

        course.put("status", "draft");
        course.put("about", EasyStringUtil.isNotBlank(fields.get("about")) ? fields.get("about") : ""); // purifyHtml($course["about"])
        course.put("tags", EasyStringUtil.isNotBlank(fields.get("tags")) ? fields.get("tags") : "");
        course.put("userId", currentUser.getId());
        course.put("createdTime", System.currentTimeMillis());
        course.put("teacherIds", fields.get("userId"));
        course = courseDao.addCourse(course);

        Map<String, Object> member = new ParamMap()
                .add("courseId", course.get("id"))
                .add("userId", course.get("userId"))
                .add("role", "teacher")
                .add("createdTime", System.currentTimeMillis()).toMap();
        //MemberDao.addMember(member);

        course = getCourse(course.get("id"));
        logService.info(currentUser, "course", "create",
                String.format("创建课程《%s》(#%d)", course.get("title"), course.get("id")));

        return course;
    }

    @Override
    public Map<String, Object> tryManageCourse(AppUser currentUser, Integer courseId) {
        if (!userAccessControlService.isLoggedIn()) {
            throw new RuntimeGoingException("未登录用户，无权操作！");
        }

        Map<String, Object> course = courseDao.getCourse(courseId);
        if (course == null || course.isEmpty()) {
            throw new RuntimeGoingException("课程不存在！");
        }

        if (!hasCourseManagerRole(courseId, currentUser.getId())) {
            throw new RuntimeGoingException("您不是课程的教师或管理员，无权操作！");
        }

        unserialize(course);
        return course;
    }

    private void courseValueToArray(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        String[] arr = String.valueOf(obj).split("\\|");
        if (arr.length > 1) {
            map.put(key, arr);
        }
    }

    public void unserialize(Map<String, Object> course) {
        if (course == null || course.isEmpty()) {
            return;
        }

        courseValueToArray(course, "tags");
        courseValueToArray(course, "goals");
        courseValueToArray(course, "audiences");
        courseValueToArray(course, "teacherIds");
    }

    private boolean hasCourseManagerRole(Integer courseId, Integer userId) {
        if (userAccessControlService.hasRole("ROLE_ADMIN")) {
            return true;
        }

//        Map<String, Object> member = memberDao.getMemberByCourseIdAndUserId(courseId, userId);
//        if (member != null && "teacher".equals(member.get("role"))) {
//            return true;
//        }

        return false;
    }

    @Override
    public void updateCourse(AppUser currentUser, Integer id, Map<String, Object> fields) {
        Map<String, Object> course = courseDao.getCourse(id);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在，更新失败！");
        }

        this.filterCourseFields(fields);

        int nums = courseDao.updateCourse(id, fields);
        if (nums > 0) {
            logService.info(currentUser, "course", "update",
                    String.format("更新课程《%s》(#%d)的信息", course.get("title"), course.get("id")), fields);
        }
    }

    private void filterCourseFields(Map<String, Object> fields) {
        if (EasyStringUtil.isBlank(fields.get("categoryId"))) {
            fields.remove("categoryId");
        }
        ArrayToolkit.filter(fields, new ParamMap()
                .add("title", "").add("subtitle", "")
                .add("about", "").add("expiryDay", 0)
                .add("serializeMode", "none")
                .add("categoryId", 0)
                .add("vipLevelId", 0)
                .add("goals", null)
                .add("audiences", null)
                .add("tags", "")
                .add("freeStartTime", 0)
                .add("freeEndTime", 0)
                .add("locationId", 0)
                .add("address", "")
                .add("maxStudentNum", 0).toMap());

        if (EasyStringUtil.isNotBlank(fields.get("about"))) {
            fields.put("about", EasyStringUtil.purifyHtml(String.valueOf(fields.get("about"))));
        }

        if (fields.get("tags") != null) {
            String[] tagNames = EasyStringUtil.explode(",", fields.get("tags"));
            List<Map<String, Object>> tagList = tagService.findTagsByNames(tagNames);
            fields.put("tags",
                    tagList.stream().map(x -> String.valueOf(x.get("id"))).collect(Collectors.joining(",")));
        }
    }

    @Override
    public List<Map<String, Object>> findCoursesByLikeTitle(Object title) {
        return new ArrayList<>(0);
    }

    @Override
    public List<Map<String, Object>> findCoursesByIds(Set<Object> ids) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findLessonsByIds(Set<Object> ids) {
        return null;
    }

    @Override
    public Integer searchMemberCount(Map<String, Object> conditions) {
        return null;
    }

    @Override
    public Integer searchLearnTime(Map<String, Object> conditions) {
        return null;
    }

    @Override
    public Integer searchLessonCount(Map<String, Object> conditions) {
        return null;
    }

    public Map<String, Object> getCourse(Object id) {
        return courseDao.getCourse(id);
    }

    @Override
    public Map<String, Object> getCourse(Integer id) {
        return courseDao.getCourse(id);
    }

    @Override
    public List<Map<String, Object>> searchLessons(Map<String, Object> conditions, OrderBy orderBy, int start, int limit) {
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getCourseItems(Object courseId) {
//        List<Map<String, Object>> lessons = lessonDao.findLessonsByCourseId(courseId);

//        List<Map<String, Object>> chapters = chapterDao.findChaptersByCourseId(courseId);

        Map<String, Object> items = new HashMap<>();
//        for (Map<String, Object> lesson : lessons) {
//            lesson.put("itemType", "lesson");
//            items.put("lesson-" + lesson.get("id"), lesson);
//        }

//        for (Map<String, Object> chapter : chapters) {
//            chapter.put("itemType", "chapter");
//            items.put("chapter-" + chapter.get("id"), chapter);
//        }

//        uasort($items, function($item1, $item2){
//            return $item1["seq"] > $item2["seq"];
//        });
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getCourseMember(Object courseId, Integer userId) {
        return null;
    }

    @Override
    public void hitCourse(Integer id) {

    }

    @Override
    public Map<String, Object> getUserLearnLessonStatuses(Integer userId, Object courseId) {
        return null;
    }

    @Override
    public Object hasFavoritedCourse(Object id) {
        return null;
    }

    @Override
    public Map<String, Object> recommendCourse(AppUser currentUser, Integer courseId, String number) {
        Map<String, Object> course = this.tryAdminCourse(currentUser, courseId);

        if (!EasyStringUtil.isNumeric(number)) {
            throw new RuntimeGoingException("推荐课程序号只能为数字！");
        }

        Map<String, Object> updateMap = new ParamMap()
                .add("recommended", 1)
                .add("recommendedSeq", number)
                .add("recommendedTime", System.currentTimeMillis()).toMap();
        int nums = courseDao.updateCourse(courseId, updateMap);

        if (nums > 0) {
            course.putAll(updateMap);
            logService.info(currentUser, "course", "recommend",
                    String.format("推荐课程《%s》(#%d),序号为%s", course.get("title"), course.get("id"), number));
        }
        return course;
    }

    @Override
    public void cancelRecommendCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryAdminCourse(currentUser, courseId);

        int nums = courseDao.updateCourse(courseId, new ParamMap()
                .add("recommended", 0).add("recommendedTime", 0).add("recommendedSeq", 0).toMap());

        if (nums > 0) {
            logService.info(currentUser, "course", "cancel_recommend",
                    String.format("取消推荐课程《%s》(#%d)", course.get("title"), course.get("id")));
        }
    }

    @Override
    public void publishCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryManageCourse(currentUser, courseId);
        courseDao.updateCourse(courseId, new ParamMap().add("status", "published").toMap());
        logService.info(currentUser, "course", "publish",
                String.format("发布课程《%s》(#%d)", course.get("title"), course.get("id")));
    }

    @Override
    public void closeCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryManageCourse(currentUser, courseId);
        courseDao.updateCourse(courseId, new ParamMap().add("status", "closed").toMap());
        logService.info(currentUser, "course", "close",
                String.format("关闭课程《%s》(#%d)", course.get("title"), course.get("id")));
    }

    @Override
    public boolean deleteCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryAdminCourse(currentUser, courseId);

        // Decrease the course lesson files usage counts, if there are files used by the course lessons.
//        List<Map<String, Object>> lessons = lessonDao.findLessonsByCourseId(courseId);

//        if (ListUtil.isNotEmpty(lessons)) {
//            Set<Object> fileIds = ArrayToolkit.column(lessons, "mediaId");
//            if (fileIds != null) {
//                uploadFileService.decreaseFileUsedCount(fileIds);
//            }
//        }

        // Delete all linked course materials (the UsedCount of each material file will also be decreaased.)
//        courseMaterialService.deleteMaterialsByCourseId(courseId);

        // Delete course related data
//        memberDao.deleteMembersByCourseId(courseId);
//        lessonDao.deleteLessonsByCourseId(courseId);
//        chapterDao.deleteChaptersByCourseId(courseId);

        courseDao.deleteCourse(courseId);

        if ("live".equals(course.get("type"))) {
//            courseLessonReplayDao.deleteLessonReplayByCourseId(courseId);
        }

        logService.info(currentUser, "course", "delete",
                String.format("删除课程《%s》(#%d)", course.get("title"), course.get("id")));

        return true;
    }

    public Map<String, Object> tryAdminCourse(AppUser user, Integer courseId) {
        Map<String, Object> course = courseDao.getCourse(courseId);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在！");
        }

        if (user == null || user.getId() == null) {
            throw new RuntimeGoingException("未登录用户，无权操作！");
        }

        if (!user.hasAnyRole("ROLE_ADMIN", "ROLE_SUPER_ADMIN")) {
            throw new RuntimeGoingException("您不是管理员，无权操作！");
        }

        return course;
    }
}
