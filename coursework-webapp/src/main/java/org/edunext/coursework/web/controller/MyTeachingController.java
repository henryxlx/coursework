package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class MyTeachingController {

    private final UserAccessControlService userAccessControlService;
    private final CourseService courseService;
    private final AppSettingService settingService;

    public MyTeachingController(UserAccessControlService userAccessControlService,
                                CourseService courseService, AppSettingService settingService) {

        this.userAccessControlService = userAccessControlService;
        this.courseService = courseService;
        this.settingService = settingService;
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
}
