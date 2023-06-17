package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.CourseThreadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class LessonQuestionPluginController {

    private final CourseService courseService;
    private final CourseThreadService threadService;
    private final AppUserService userService;

    public LessonQuestionPluginController(CourseService courseService,
                                          CourseThreadService threadService,
                                          AppUserService userService) {

        this.courseService = courseService;
        this.threadService = threadService;
        this.userService = userService;
    }

    @RequestMapping("/lessonplugin/question/init")
    public String initAction(Integer courseId, Integer lessonId, HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        this.courseService.tryTakeCourse(courseId, currentUser);

        Map<String, Object> lesson = new ParamMap().add("id", lessonId).add("courseId", courseId).toMap();

        List<Map<String, Object>> threads = this.threadService.searchThreads(new ParamMap()
                        .add("lessonId", lesson.get("id")).add("type", "question").toMap(),
                "createdNotStick", 0, 20);
        model.addAttribute("threads", threads);
        model.addAttribute("users", this.userService.findUsersByIds(ArrayToolkit.column(threads, "userId")));
        model.addAttribute("lesson", lesson);
        model.addAttribute("courseId", courseId);
        model.addAttribute("lessonId", lessonId);

        return "/lesson/plugin/question/index";
    }

    @PostMapping("lessonplugin/question/create")
    public String createAction(HttpServletRequest request, Model model) {
        Map<String, Object> question = ParamMap.toQueryAllMap(request);
        question.put("type", "question");
        question.put("title", question.get("question[title]"));
        question.put("content", question.get("question[content]"));
        question.remove("question[title]");
        question.remove("question[content]");
        question.remove("_csrf_token");

        AppUser currentUser = AppUser.getCurrentUser(request);
        model.addAttribute("thread", this.threadService.createThread(question, currentUser));
        model.addAttribute("user", currentUser);
        return "/lesson/plugin/question/item";
    }

}
