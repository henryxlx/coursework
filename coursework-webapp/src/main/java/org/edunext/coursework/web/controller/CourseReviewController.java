package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppUserService;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseReviewController {

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final AppUserService userService;

    public CourseReviewController(CourseService courseService,
                                  ReviewService reviewService,
                                  AppUserService userService) {

        this.courseService = courseService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @RequestMapping("/course/{id}/review/list")
    public String listAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = this.courseService.getCourse(id);

        String previewAs = request.getParameter("previewAs");
        String isModal = request.getParameter("isModal");

        Paginator paginator = new Paginator(request, this.reviewService.getCourseReviewCount(id), 10);

        List<Map<String, Object>> reviews = this.reviewService.findCourseReviews(id,
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        model.addAttribute("users", this.userService.findUsersByIds(ArrayToolkit.column(reviews, "userId")));
        model.addAttribute("course", course);
        model.addAttribute("reviews", reviews);
        model.addAttribute("isModal", isModal);
        model.addAttribute("previewAs", previewAs);
        model.addAttribute("paginator", paginator);
        return "/course/review/list";
    }
}