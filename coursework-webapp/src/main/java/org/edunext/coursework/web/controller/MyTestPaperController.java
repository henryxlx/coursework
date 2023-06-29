package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.TestPaperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Controller
public class MyTestPaperController {

    private final TestPaperService testPaperService;
    private final CourseService courseService;

    public MyTestPaperController(TestPaperService testPaperService, CourseService courseService) {
        this.testPaperService = testPaperService;
        this.courseService = courseService;
    }

    @RequestMapping({"/my/quiz", "/my/favorite/question/show"})
    public String indexAction(HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);

        Paginator paginator = new Paginator(request,
                this.testPaperService.findTestpaperResultsCountByUserId(user.getId()), 10);

        List<Map<String, Object>> testpaperResults = this.testPaperService.findTestpaperResultsByUserId(
                user.getId(),
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        Set<Object> testpapersIds = ArrayToolkit.column(testpaperResults, "testId");

        List<Map<String, Object>> testpapers = this.testPaperService.findTestpapersByIds(testpapersIds);
        model.addAttribute("myTestpapers", ArrayToolkit.index(testpapers, "id"));

        Set<Object> targets = ArrayToolkit.column(testpapers, "target");
        Set<Object> courseIds = new HashSet<>();
        targets.forEach(target -> parseCourseIdFromTarget(courseIds, target));

        model.addAttribute("courses", this.courseService.findCoursesByIds(courseIds));

        model.addAttribute("myQuizActive", "active");
        model.addAttribute("user", user);
        model.addAttribute("myTestpaperResults", testpaperResults);
        model.addAttribute("paginator", paginator);

        return "/my/quiz/my-quiz";
    }

    private void parseCourseIdFromTarget(Set<Object> courseIds, Object target) {
        String course = String.valueOf(target);
        String[] arr = course.split("/");
        course = arr != null && arr.length > 0 ? arr[0] : null;
        if (course != null) {
            arr = course.split("-");
            course = arr != null && arr.length > 1 ? arr[1] : null;
            if (course != null) {
                courseIds.add(course);
            }
        }
    }

    @RequestMapping("/my/teacher/{target}/test/list")
    public String listReviewingTestAction(HttpServletRequest request) {
        return "/my/quiz/teacher-test-layout";
    }
}
