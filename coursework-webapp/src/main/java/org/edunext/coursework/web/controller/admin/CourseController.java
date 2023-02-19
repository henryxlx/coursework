package org.edunext.coursework.web.controller.admin;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xulixin
 */
@Controller("adminCourseController")
public class CourseController {

    private final CourseService courseService;
    private final AppCategoryService categoryService;
    private final AppUserService userService;
    private final AppSettingService settingService;

    public CourseController(CourseService courseService,
                            AppCategoryService categoryService,
                            AppUserService userService,
                            AppSettingService settingService) {

        this.courseService = courseService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.settingService = settingService;
    }

    @RequestMapping("/admin/course")
    public String indexPage(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = ParamMap.toConditionMap(request);
        int count = courseService.searchCourseCount(conditions);

        Paginator paginator = new Paginator(request, count, 20);
        List<Map<String, Object>> courses = courseService.searchCourses(conditions, null,
                paginator.getOffsetCount(),  paginator.getPerPageCount());

        model.addAttribute("categories",
                categoryService.findCategoriesByIds(ArrayToolkit.column(courses, "categoryId")));

        model.addAttribute("users", userService.findUsersByIds(ArrayToolkit.column(courses, "userId")));

        Map<String, Object> courseSetting = settingService.get("course");
        if (Objects.isNull(courseSetting.get("live_course_enabled"))) {
            courseSetting.put("live_course_enabled", "");
        }

        model.addAttribute("conditions", conditions);
        model.addAttribute("courses", courses);
        model.addAttribute("paginator", paginator);
        model.addAttribute("liveSetEnabled", courseSetting.get("live_course_enabled"));
        model.addAttribute("default", settingService.get("default"));
        model.addAttribute("categoryForCourse", categoryService.buildCategoryChoices("course"));

        return "/admin/course/index";
    }

    @RequestMapping("/admin/course/recommend/list")
    public String recommendListAction(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = new ParamMap()
                .add("status", "published").add("recommended", 1).toMap();

        Paginator paginator = new Paginator(request, courseService.searchCourseCount(conditions), 20);

        List<Map<String, Object>> courses = courseService.searchCourses(conditions, "recommendedSeq",
                paginator.getOffsetCount(), paginator.getPerPageCount());

        model.addAttribute("courses", courses);
        model.addAttribute("users", userService.findUsersByIds(ArrayToolkit.column(courses, "userId")));
        model.addAttribute("paginator", paginator);

        return "/admin/course/course-recommend-list";
    }
}
