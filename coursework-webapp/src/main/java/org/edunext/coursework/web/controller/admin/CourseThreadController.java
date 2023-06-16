package org.edunext.coursework.web.controller.admin;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.ListUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.CourseThreadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Controller
public class CourseThreadController {

    private final CourseService courseService;
    private final CourseThreadService threadService;
    private final AppUserService userService;

    public CourseThreadController(CourseService courseService,
                                  CourseThreadService threadService,
                                  AppUserService userService) {

        this.courseService = courseService;
        this.threadService = threadService;
        this.userService = userService;
    }

    @RequestMapping("/admin/course/thread")
    public String indexAction(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = ParamMap.toConditionMap(request);

        if ("courseTitle".equals(conditions.get("keywordType"))) {
            List<Map<String, Object>> courses = courseService.findCoursesByLikeTitle(conditions.get("keyword"));
            Set<Object> courseIds = ArrayToolkit.column(courses, "id");
            conditions.put("courseIds", courseIds);
            if (courseIds.isEmpty()){
                model.addAttribute("threads", ListUtil.newArrayList());
                model.addAttribute("users", MapUtil.newHashMap());
                model.addAttribute("courses", ListUtil.newArrayList());
                model.addAttribute("lessons", ListUtil.newArrayList());
                model.addAttribute("paginator", new Paginator(request,0,20));
                return "admin/course/thread/index";
            }
        }

        Paginator paginator = new Paginator(request, threadService.searchThreadCount(conditions), 20);
        List<Map<String, Object>> threads = threadService.searchThreads(conditions, "createdNotStick",
                paginator.getOffsetCount(), paginator.getPerPageCount());
        model.addAttribute("users", userService.findUsersByIds(ArrayToolkit.column(threads, "userId")));
        model.addAttribute("courses", courseService.findCoursesByIds(ArrayToolkit.column(threads, "courseId")));
        model.addAttribute("lessons", courseService.findLessonsByIds(ArrayToolkit.column(threads, "lessonId")));
        model.addAttribute("paginator", paginator);
        model.addAttribute("threads", threads);

        return "admin/course/thread/index";
    }
}
