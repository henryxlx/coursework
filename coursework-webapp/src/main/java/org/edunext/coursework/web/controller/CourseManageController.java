package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import com.jetwinner.webfast.module.bigapp.service.AppTagService;
import com.jetwinner.webfast.session.FlashMessageUtil;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller("frontCourseManageController")
public class CourseManageController {

    private final CourseService courseService;
    private final AppCategoryService categoryService;
    private final AppSettingService settingService;
    private final AppTagService tagService;

    public CourseManageController(CourseService courseService,
                                  AppCategoryService categoryService,
                                  AppSettingService settingService,
                                  AppTagService tagService) {

        this.courseService = courseService;
        this.categoryService = categoryService;
        this.settingService = settingService;
        this.tagService = tagService;
    }

    @RequestMapping("/course/{id}/manage")
    public String indexAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        return baseAction(id, request, model);
    }

    @RequestMapping("/course/{id}/manage/base")
    public String baseAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        Map<String, Object> courseSetting = settingService.get("course");
        if("POST".equals(request.getMethod())){
            Map<String, Object> data = ParamMap.toFormDataMap(request);
            courseService.updateCourse(id, data);
            FlashMessageUtil.setFlashMessage("success", "课程基本信息已保存！", request.getSession());
            return String.format("redirect:/course/%d/manage/base", id);
        }

        List<Map<String, Object>> tags = tagService.findTagsByIds((String[]) course.get("tags"));
        model.addAttribute("tags", ArrayToolkit.column(tags, "name"));
        model.addAttribute("default", settingService.get("default"));
        model.addAttribute("course", course);
        model.addAttribute("categoryForCourse", categoryService.buildCategoryChoices("course"));

        return "/course/manage/base";
    }
}
