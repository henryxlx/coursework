package org.edunext.coursework.web.controller;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyCourseController {

    private final CourseService courseService;

    public MyCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @RequestMapping("/my/courses/learning")
    public String learningAction(HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Paginator paginator = new Paginator(request,
                courseService.findUserLeaningCourseCount(currentUser.getId()),
                12);

        model.addAttribute("courses", courseService.findUserLeaningCourses(currentUser.getId(),
                paginator.getOffsetCount(), paginator.getPerPageCount()));

        model.addAttribute("paginator", paginator);

        return "/my/course/learning";
    }
}
