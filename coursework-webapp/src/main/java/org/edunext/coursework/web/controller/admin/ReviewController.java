package org.edunext.coursework.web.controller.admin;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jingjianxin
 */
@Controller("adminReviewController")
@RequestMapping("/admin/course/review")
public class ReviewController {

    private static final String VIEW_PREFIX = "/admin/course/review/";

    private final ReviewService reviewService;
    private final CourseService courseService;
    private final AppUserService userService;

    public ReviewController(ReviewService reviewService,
                            CourseService courseService,
                            AppUserService userService) {

        this.reviewService = reviewService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @RequestMapping({"", "/"})
    public String indexPage(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = ParamMap.toConditionMap(request);

        if (EasyStringUtil.isNotBlank(conditions.get("courseTitle"))) {
            List<Map<String, Object>> courses = this.courseService.findCoursesByLikeTitle(conditions.get("courseTitle"));
            Set<Object> courseIds = ArrayToolkit.column(courses, "id");
            conditions.put("courseIds", courseIds);
            if (courseIds == null || courseIds.size() == 0) {
                model.addAttribute("reviews", null);
                model.addAttribute("users", null);
                model.addAttribute("courses", null);
                model.addAttribute("paginator", new Paginator(request, 0, 20));
                return VIEW_PREFIX + "index";
            }
        }

        Paginator paginator = new Paginator(request, this.reviewService.searchReviewsCount(conditions), 20);
        List<Map<String, Object>> reviews = this.reviewService.searchReviews(conditions, "latest",
                paginator.getOffsetCount(), paginator.getPerPageCount());

        model.addAttribute("users",
                this.userService.findUsersByIds(ArrayToolkit.column(reviews, "userId")));

        List<Map<String, Object>> courses =
                this.courseService.findCoursesByIds(ArrayToolkit.column(reviews, "courseId"));
        model.addAttribute("courses", ArrayToolkit.index(courses, "id"));

        model.addAttribute("reviews", reviews);
        model.addAttribute("paginator", paginator);

        return VIEW_PREFIX + "index";
    }

}
