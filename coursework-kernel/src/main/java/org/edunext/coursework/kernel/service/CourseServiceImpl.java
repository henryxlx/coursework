package org.edunext.coursework.kernel.service;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.toolbag.MapKitOnJava8;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.dao.support.OrderByBuilder;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppLogService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import org.edunext.coursework.kernel.dao.CourseDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author xulixin
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final AppUserService userService;
    private final UserAccessControlService userAccessControlService;
    private final AppCategoryService categoryService;
    private final CourseDao courseDao;
    private final AppLogService logService;

    public CourseServiceImpl(AppUserService userService,
                             UserAccessControlService userAccessControlService,
                             AppCategoryService categoryService,
                             CourseDao courseDao,
                             AppLogService logService) {

        this.userService = userService;
        this.userAccessControlService = userAccessControlService;
        this.categoryService = categoryService;
        this.courseDao = courseDao;
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
        OrderByBuilder orderByBuilder;
        if ("popular".equals(sort)) {
            orderByBuilder = OrderBy.builder().addDesc("hitNum");
        } else if ("recommended".equals(sort)) {
            orderByBuilder = OrderBy.builder().addDesc("recommendedTime");
        } else if ("Rating".equals(sort)) {
            orderByBuilder = OrderBy.builder().addDesc("Rating");
        } else if ("hitNum".equals(sort)) {
            orderByBuilder = OrderBy.builder().addDesc("hitNum");
        } else if ("studentNum".equals(sort)) {
            orderByBuilder = OrderBy.builder().addDesc("studentNum");
        } else if ("recommendedSeq".equals(sort)) {
            orderByBuilder = OrderBy.builder().add("recommendedSeq");
        } else if ("createdTimeByAsc".equals(sort)) {
            orderByBuilder = OrderBy.builder().add("createdTime");
        } else {
            orderByBuilder = OrderBy.builder().addDesc("createdTime");
        }

        return courseDao.searchCourses(conditions, orderByBuilder, start, limit);
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
        //memberDao.addMember(member);

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
        String[] arr;
        if (obj != null) {
            String s = String.valueOf(obj);
            s = s.length() > 0 ? s.substring(1) : s;
            arr = s.split("\\|");
        } else {
            arr = new String[0];
        }
        map.put(key, arr);
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
    public void updateCourse(Integer id, Map<String, Object> fields) {

    }

    public Map<String, Object> getCourse(Object id) {
        return courseDao.getCourse(id);
    }
}
