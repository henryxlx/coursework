package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import com.jetwinner.webfast.module.bigapp.service.AppTagService;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
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
    private final AppUserService userService;

    public CourseManageController(CourseService courseService, AppUserService userService,
                                  AppCategoryService categoryService,
                                  AppSettingService settingService,
                                  AppTagService tagService) {

        this.userService = userService;
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
        if ("POST".equals(request.getMethod())) {
            Map<String, Object> data = ParamMap.toFormDataMap(request);
            courseService.updateCourse(AppUser.getCurrentUser(request), id, data);
            BaseControllerHelper.setFlashMessage("success", "课程基本信息已保存！", request.getSession());
            return String.format("redirect:/course/%d/manage/base", id);
        }

        List<Map<String, Object>> tags = tagService.findTagsByIds(EasyStringUtil.explode(",", course.get("tags")));
        model.addAttribute("tags", ArrayToolkit.column(tags, "name"));
        model.addAttribute("default", settingService.get("default"));
        model.addAttribute("course", course);
        model.addAttribute("categoryForCourse", categoryService.buildCategoryChoices("course"));

        return "/course/manage/base";
    }

    @RequestMapping("/course/{id}/manage/detail")
    public String detailAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> detail = ParamMap.toFormDataMap(request);
            if (!detail.containsKey("goals")) {
                detail.put("goals", "");
            }
            if (!detail.containsKey("audiences")) {
                detail.put("audiences", "");
            }

            courseService.updateCourse(AppUser.getCurrentUser(request), id, detail);
            BaseControllerHelper.setFlashMessage("success", "课程详细信息已保存！", request.getSession());

            return String.format("redirect:/course/%d/manage/detail", id);
        }

        model.addAttribute("course", course);
        return "/course/manage/detail";
    }

    @RequestMapping("/course/{id}/manage/picture")
    public String pictureAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);

        model.addAttribute("course", course);
        return "/course/manage/picture";
    }

    @RequestMapping("/course/{id}/manage/lesson")
    public String lessonAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course",course);
        return "/course/manage/lesson/index";
    }

    @RequestMapping("/course/{id}/manage/files")
    public String fileAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course",course);
        return "/course/manage/files/index";
    }

    @RequestMapping("/course/{id}/manage/teachers")
    public String teachersAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course",course);
        return "/course/manage/teachers";
    }

    @RequestMapping("/course/{id}/manage/students")
    public String studentsAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course",course);
        return "/course/manage/students/index";
    }

    @RequestMapping("/course/{id}/manage/question")
    public String questionAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course",course);
        return "/course/manage/question/index";
    }

    @RequestMapping("/course/{id}/manage/testpaper")
    public String testpaperAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course",course);
        return "/course/manage/testpaper/index";
    }

    @RequestMapping("/course/{id}/manage/data")
    public String dataAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course",course);
        return "/course/manage/data";
    }

    @RequestMapping("/course/{id}/manage/myquiz/list_course_test_paper")
    public String checkAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course",course);
        return "/course/manage/myquiz/list_course_test_paper";
    }

}


