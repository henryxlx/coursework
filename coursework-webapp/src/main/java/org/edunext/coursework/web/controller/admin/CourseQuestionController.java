package org.edunext.coursework.web.controller.admin;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.CourseThreadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Controller
public class CourseQuestionController {

    private final CourseService courseService;
    private final CourseThreadService threadService;
    private final AppUserService userService;

    public CourseQuestionController(CourseService courseService,
                                    CourseThreadService threadService,
                                    AppUserService userService) {

        this.courseService = courseService;
        this.threadService = threadService;
        this.userService = userService;
    }


    @RequestMapping("/admin/course/question")
    public String indexPage(@RequestParam String postStatus, HttpServletRequest request, Model model) {
        Map<String, Object> conditions = ParamMap.toQueryAllMap(request);
        if ("courseTitle".equals(conditions.get("keywordType"))) {
            String keyword = String.valueOf(conditions.get("keyword"));
            List<Map<String, Object>> courses = this.courseService.findCoursesByLikeTitle(keyword.trim());
            Set<Object> courseIds = ArrayToolkit.column(courses, "id");
            conditions.put("courseIds", courseIds);
            if (courseIds == null || courseIds.size() == 0) {
                model.addAttribute("type", postStatus);
                return "/course/question/index";
            }
        }
        conditions.put("type", "question");

        Paginator paginator = new Paginator(request, this.threadService.searchThreadCount(conditions), 20);

        List<Map<String, Object>> questions = this.threadService.searchThreads(conditions,
                "createdNotStick",
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        List<Map<String, Object>> unPostedQuestion = new ArrayList<>();
        for (Map<String, Object> value : questions) {
            if (ValueParser.parseInt(value.get("postNum")) == 0) {
                unPostedQuestion.add(value);
            } else {
                int threadPostsNum = this.threadService.getThreadPostCountByThreadId(ValueParser.toInteger(value.get("id")));
                int userPostsNum = this.threadService.getPostCountByUserIdAndThreadId(
                        ValueParser.toInteger(value.get("userId")), ValueParser.toInteger(value.get("id")));
                if (userPostsNum == threadPostsNum) {
                    unPostedQuestion.add(value);
                }
            }
        }

        if ("unPosted".equals(postStatus)) {
            questions = unPostedQuestion;
        }
        model.addAttribute("questions", questions);
        model.addAttribute("users", this.userService.findUsersByIds(ArrayToolkit.column(questions, "userId")));
        model.addAttribute("courses", ArrayToolkit.index(
                this.courseService.findCoursesByIds(ArrayToolkit.column(questions, "courseId")), "id"));
        model.addAttribute("lessons", ArrayToolkit.index(
                this.courseService.findLessonsByIds(ArrayToolkit.column(questions, "lessonId")), "id"));
        model.addAttribute("paginator", paginator);
        model.addAttribute("type", postStatus);
        return "/admin/course/question/index";
    }

}
