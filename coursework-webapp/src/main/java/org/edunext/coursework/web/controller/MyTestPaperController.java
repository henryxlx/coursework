package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.TestPaperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    private final AppUserService userService;
    private final UserAccessControlService userAccessControlService;

    public MyTestPaperController(TestPaperService testPaperService,
                                 CourseService courseService,
                                 AppUserService userService,
                                 UserAccessControlService userAccessControlService) {

        this.testPaperService = testPaperService;
        this.courseService = courseService;
        this.userService = userService;
        this.userAccessControlService = userAccessControlService;
    }

    @RequestMapping("/my/quiz")
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

        model.addAttribute("courses", ArrayToolkit.index(this.courseService.findCoursesByIds(courseIds), "id"));

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

    @RequestMapping("/my/teacher/reviewing/test/list")
    public ModelAndView listReviewingTestAction(HttpServletRequest request) {
        AppUser user = AppUser.getCurrentUser(request);

        if (!userAccessControlService.hasRole("ROLE_TEACHER")) {
            return BaseControllerHelper.createMessageResponse("error", "您不是老师，不能查看此页面！");
        }

        List<Map<String, Object>> courses = this.courseService.findUserTeachCourses(user.getId(),
                0, Integer.MAX_VALUE, false);

        Set<Object> courseIds = ArrayToolkit.column(courses, "id");
        List<Map<String, Object>> testpapers = this.testPaperService.findAllTestpapersByTargets(courseIds);
        Set<Object> testpaperIds = ArrayToolkit.column(testpapers, "id");

        Paginator paginator = new Paginator(request,
                this.testPaperService.findTestpaperResultCountByStatusAndTestIds(testpaperIds, "reviewing"),
                10);

        List<Map<String, Object>> paperResults = this.testPaperService.findTestpaperResultsByStatusAndTestIds(
                testpaperIds,
                "reviewing",
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        testpaperIds = ArrayToolkit.column(paperResults, "testId");

        testpapers = this.testPaperService.findTestpapersByIds(testpaperIds);

        Set<Object> userIds = ArrayToolkit.column(paperResults, "userId");

        Map<String, AppUser> users = this.userService.findUsersByIds(userIds);

        Set<Object> targets = ArrayToolkit.column(testpapers, "target");
        final Set<Object> courseIdsFromTarget = new HashSet<>(targets.size());
        targets.forEach(obj -> {
            String target = String.valueOf(obj);
            String[] arr = target.split("/");
            arr = arr[0].split("-");
            courseIdsFromTarget.add(arr[1]);
        });

        courses = this.courseService.findCoursesByIds(courseIdsFromTarget);

        ModelAndView mav = new ModelAndView("/my/quiz/teacher-test-layout");
        mav.addObject("status", "reviewing");
        mav.addObject("users", users);
        mav.addObject("paperResults", ArrayToolkit.index(paperResults, "id"));
        mav.addObject("courses", ArrayToolkit.index(courses, "id"));
        mav.addObject("testpapers", ArrayToolkit.index(testpapers, "id"));
        mav.addObject("teacher", user);
        mav.addObject("paginator", paginator);
        return mav;
    }

    @RequestMapping("/my/teacher/finished/test/list")
    public String listFinishedTestAction(@PathVariable String status, HttpServletRequest request, Model model) {
        model.addAttribute("status", "finished");
        return "/my/quiz/teacher-test-layout";
    }
}
