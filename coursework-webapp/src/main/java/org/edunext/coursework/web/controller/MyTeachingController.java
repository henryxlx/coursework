package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.CourseThreadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author xulixin
 */
@Controller
public class MyTeachingController {

    private final UserAccessControlService userAccessControlService;
    private final CourseService courseService;
    private final CourseThreadService courseThreadService;
    private final AppSettingService settingService;
    private final AppUserService userService;

    public MyTeachingController(UserAccessControlService userAccessControlService,
                                CourseService courseService,
                                CourseThreadService courseThreadService,
                                AppSettingService settingService,
                                AppUserService userService) {

        this.userAccessControlService = userAccessControlService;
        this.courseService = courseService;
        this.courseThreadService = courseThreadService;
        this.settingService = settingService;
        this.userService = userService;
    }

    @RequestMapping("/my/teaching/courses")
    public ModelAndView coursesAction(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/my/teaching/teaching");
        AppUser user = AppUser.getCurrentUser(request);

        if (!userAccessControlService.hasRole("ROLE_TEACHER")) {
            return BaseControllerHelper.createMessageResponse("error", "您不是老师，不能查看此页面！");
        }

        Paginator paginator = new Paginator(request,
                courseService.findUserTeachCourseCount(user.getId(), false), 12);

        mav.addObject("courses", courseService.findUserTeachCourses(user.getId(),
                paginator.getOffsetCount(), paginator.getPerPageCount(), false));

        mav.addObject("paginator", paginator);

        Map<String, Object> courseSetting = settingService.get("course");
        mav.addObject("live_course_enabled",
                EasyStringUtil.isBlank(courseSetting.get("live_course_enabled")) ? '0' :
                        courseSetting.get("live_course_enabled"));

        return mav;
    }

    @RequestMapping("/my/teaching/threads/{type}")
    public ModelAndView threadsAction(@PathVariable String type, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/my/teaching/threads");
        AppUser user = AppUser.getCurrentUser(request);

        if (!userAccessControlService.hasRole("ROLE_TEACHER")) {
            return BaseControllerHelper.createMessageResponse("error", "您不是老师，不能查看此页面！");
        }

        int myTeachingCourseCount = this.courseService.findUserTeachCourseCount(user.getId(), true);

        if (myTeachingCourseCount < 1) {
            mav.addObject("type", type);
            mav.addObject("threadType", "course");
            mav.addObject("threads", new ArrayList<>(0));
        }

        List<Map<String, Object>> myTeachingCourses = this.courseService.findUserTeachCourses(user.getId(),
                0, myTeachingCourseCount < 1 ? 10 : myTeachingCourseCount, true);

        Map<String, Object> conditions = new HashMap<>(2);
        Set<Object> courseIds = ArrayToolkit.column(myTeachingCourses, "id");
        if (courseIds.size() > 0) {
            conditions.put("courseIds", courseIds);
        }
        conditions.put("type", type);

        Paginator paginator = new Paginator(request,
                this.courseThreadService.searchThreadCountInCourseIds(conditions), 20);

        List<Map<String, Object>> threads = this.courseThreadService.searchThreadInCourseIds(conditions,
                "createdNotStick",
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        mav.addObject("users",
                this.userService.findUsersByIds(ArrayToolkit.column(threads, "latestPostUserId")));
        mav.addObject("courses",
                ArrayToolkit.index(this.courseService.findCoursesByIds(ArrayToolkit.column(threads, "courseId")), "Id"));
        mav.addObject("lessons",
                ArrayToolkit.index(this.courseService.findLessonsByIds(ArrayToolkit.column(threads, "lessonId")), "id"));

        mav.addObject("paginator", paginator);
        mav.addObject("threads", threads);
        mav.addObject("type", type);
        mav.addObject("threadType", "course");
        return mav;
    }
}
