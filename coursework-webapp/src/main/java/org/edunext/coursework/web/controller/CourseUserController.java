package org.edunext.coursework.web.controller;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppUserService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xulixin
 */
@Controller("frontCourseUserController")
public class CourseUserController {

    private final CourseService courseService;
    private final AppUserService userService;

    public CourseUserController(CourseService courseService, AppUserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @RequestMapping("/user/{id}/teach")
    public ModelAndView teachAction(@PathVariable Integer id, HttpServletRequest request) {
        AppUser user = userService.getUser(id);
        if (user == null) {
            throw new RuntimeGoingException("用户未找到！");
        }
        return this._teachAction(user, request);
    }

    private ModelAndView _teachAction(AppUser user, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/user/course");
        Paginator paginator = new Paginator(request,
                this.courseService.findUserTeachCourseCount(user.getId(), true), 10);

        mav.addObject("courses", this.courseService.findUserTeachCourses(user.getId(),
                paginator.getOffsetCount(),
                paginator.getPerPageCount(), true));

        mav.addObject("user", user);
        mav.addObject("paginator", paginator);
        mav.addObject("type", "teach");
        return mav;
    }

    @RequestMapping("/user/{id}/learn")
    public ModelAndView learnAction(@PathVariable Integer id, HttpServletRequest request) {
        AppUser user = userService.getUser(id);
        if (user == null) {
            throw new RuntimeGoingException("用户未找到！");
        }
        return this._learnAction(user, request);
    }

    private ModelAndView _learnAction(AppUser user, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/user/course");
        Paginator paginator = new Paginator(request,
                this.courseService.findUserLearnCourseCount(user.getId()), 10);

        mav.addObject("courses", this.courseService.findUserLearnCourses(user.getId(),
                paginator.getOffsetCount(),
                paginator.getPerPageCount()));

        mav.addObject("user", user);
        mav.addObject("paginator", paginator);
        mav.addObject("type", "learn");
        return mav;
    }

    @RequestMapping("/user/{id}/favorited")
    public String favoritedAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        AppUser user = userService.getUser(id);
        if (user == null) {
            throw new RuntimeGoingException("用户未找到！");
        }
        Paginator paginator = new Paginator(request,
                this.courseService.findUserFavoritedCourseCount(user.getId()), 10);

        model.addAttribute("courses", this.courseService.findUserFavoritedCourses(user.getId(),
                paginator.getOffsetCount(), paginator.getPerPageCount()));

        model.addAttribute("user", user);
        model.addAttribute("paginator", paginator);
        model.addAttribute("type", "favorited");
        return "/user/course";
    }
}
