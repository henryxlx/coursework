package org.edunext.coursework.web.controller;

import com.jetwinner.webfast.kernel.AppUser;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class TestPaperController {

    private final CourseService courseService;

    public TestPaperController(CourseService courseService) {
        this.courseService = courseService;
    }

    @RequestMapping("/course/{id}/check/{status}/list")
    public String teacherCheckInCourseAction(@PathVariable Integer id, @PathVariable String status,
                                             HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course", course);
        return "/my/quiz/list-course-test-paper";
    }
}
