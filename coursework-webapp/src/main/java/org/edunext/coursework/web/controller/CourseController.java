package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller("frontCourseController")
public class CourseController {

    private final AppUserService userService;
    private final UserAccessControlService userAccessControlService;
    private final CourseService courseService;

    public CourseController(AppUserService userService,
                            UserAccessControlService userAccessControlService,
                            CourseService courseService) {

        this.userService = userService;
        this.userAccessControlService = userAccessControlService;
        this.courseService = courseService;
    }

    @RequestMapping("/course/archive")
    public String archivePage() {
        return "/course/archive";
    }

    @RequestMapping("/course/create")
    public String createAction(HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);
        model.addAttribute("userProfile", userService.getUserProfile(user.getId()));

        String isLive = request.getParameter("flag");
        String type = "isLive".equals(isLive) ? "live" : "normal";

        if (userAccessControlService.hasRole("ROLE_TEACHER") == false) {
            throw new RuntimeGoingException("没有权限不能访问！");
        }

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> course = ParamMap.toFormDataMap(request);
            course = courseService.createCourse(user, course);
            return String.format("redirect:/course/%s/manage", course.get("id"));
        }

        model.addAttribute("type", type);
        return "/course/create";
    }
}
