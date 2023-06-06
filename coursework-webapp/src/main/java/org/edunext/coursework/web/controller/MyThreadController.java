package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.ThreadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class MyThreadController {

    private final ThreadService threadService;
    private final CourseService courseService;
    private final AppUserService userService;

    public MyThreadController(ThreadService threadService,
                              CourseService courseService,
                              AppUserService userService) {

        this.threadService = threadService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @RequestMapping("/my/discussions")
    public String discussionsAction(HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);

        Map<String, Object> conditions = new ParamMap()
                .add("userId", user.getId()).add("type", "discussion").toMap();

        Paginator paginator = new Paginator(request, this.threadService.searchThreadCount(conditions), 20);

        List<Map<String, Object>> threads = this.threadService.searchThreads(
                conditions,
                "createdNotStick",
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        model.addAttribute("courses",
                this.courseService.findCoursesByIds(ArrayToolkit.column(threads, "courseId")));
        model.addAttribute("users",
                this.userService.findUsersByIds(ArrayToolkit.column(threads, "latestPostUserId")));

        model.addAttribute("threadType", "course");
        model.addAttribute("threads", threads);
        model.addAttribute("paginator", paginator);
        return "/my/thread/discussions";
    }

    @RequestMapping("/my/questions")
    public String questionsAction(HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);

        Map<String, Object> conditions = new ParamMap()
                .add("userId", user.getId()).add("type", "'question'").toMap();

        Paginator paginator = new Paginator(request, this.threadService.searchThreadCount(conditions), 20);

        List<Map<String, Object>> threads = this.threadService.searchThreads(
                conditions,
                "createdNotStick",
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        model.addAttribute("courses",
                this.courseService.findCoursesByIds(ArrayToolkit.column(threads, "courseId")));
        model.addAttribute("users",
                this.userService.findUsersByIds(ArrayToolkit.column(threads, "latestPostUserId")));

        model.addAttribute("threads", threads);
        model.addAttribute("paginator", paginator);
        return "/my/thread/questions";
    }
}
