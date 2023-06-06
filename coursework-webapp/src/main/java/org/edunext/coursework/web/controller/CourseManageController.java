package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.toolbag.FileToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.image.ImageSize;
import com.jetwinner.webfast.image.ImageUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.FastAppConst;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import com.jetwinner.webfast.module.bigapp.service.AppTagService;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import com.jetwinner.webfast.mvc.extension.WebExtensionPack;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller("frontCourseManageController")
public class CourseManageController {

    private final CourseService courseService;
    private final AppCategoryService categoryService;
    private final AppSettingService settingService;
    private final FastAppConst appConst;
    private final AppTagService tagService;
    private final AppUserService userService;

    public CourseManageController(CourseService courseService,
                                  AppCategoryService categoryService,
                                  AppSettingService settingService,
                                  FastAppConst appConst,
                                  AppTagService tagService,
                                  AppUserService userService) {

        this.courseService = courseService;
        this.categoryService = categoryService;
        this.settingService = settingService;
        this.appConst = appConst;
        this.tagService = tagService;
        this.userService = userService;
    }

    @RequestMapping("/course/{id}/manage")
    public String indexAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        return baseAction(id, request, model);
    }

    @RequestMapping("/course/{id}/manage/base")
    public String baseAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
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
    public ModelAndView pictureAction(@PathVariable Integer id, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        if ("POST".equals(request.getMethod())) {
            MultipartHttpServletRequest requestForMime = (MultipartHttpServletRequest) request;
            MultipartFile file = requestForMime.getFile("picture");
            if (file == null) {
                return BaseControllerHelper.createMessageResponse("error", "没有可供上传的文件，请选择文件。");
            }
            if (!FileToolkit.isImageFile(file.getOriginalFilename())) {
                return BaseControllerHelper.createMessageResponse("error", "上传图片格式错误，请上传jpg, gif, png格式的文件。");
            }

            String filenamePrefix = "course_" + course.get("id") + "_";
            String hash = FileToolkit.hashFilename(filenamePrefix);
            String ext = FileToolkit.getFileExtension(file.getOriginalFilename());
            String filename = filenamePrefix + hash + '.' + ext;

            String directory = appConst.getUploadPublicDirectory() + "/tmp";
            try {
                FileToolkit.transferFile(file, directory, filename);
            } catch (IOException e) {
                return BaseControllerHelper.createMessageResponse("error",
                        String.format("图片上传失败，请检查上传目录(%s)或文件(%s)是否存在。", directory,
                                file.getOriginalFilename()));
            }

            filename = filename.replace(".", "!");

            mav.addObject("id", course.get("id"));
            mav.addObject("file", filename);
            mav.setViewName(String.format("redirect:/course/%d/manage/picture/crop", course.get("id")));
            return mav;
        }

        mav.addObject("course", course);
        mav.setViewName("/course/manage/picture");
        return mav;
    }

    @RequestMapping("/course/{id}/manage/picture/crop")
    public ModelAndView pictureCropAction(@PathVariable Integer id, HttpServletRequest request) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);

        String filename = request.getParameter("file");
        filename = filename.replace("!", ".");
//        filename = filename.replace("[..|/|\\]", "");

        String pictureFilePath = appConst.getUploadPublicDirectory() + "/tmp/" + filename;

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> c = ParamMap.toQueryAllMap(request);
            courseService.changeCoursePicture(AppUser.getCurrentUser(request), course.get("id"), pictureFilePath, c);
            return new ModelAndView(String.format("redirect:/course/%d/manage/picture", course.get("id")));
        }

        ModelAndView mav = new ModelAndView("/course/manage/picture-crop");
        ImageSize imageSize;
        try {
            imageSize = ImageUtil.getNaturalSize(new File(pictureFilePath));
        } catch (Exception e) {
            return BaseControllerHelper.createMessageResponse("error", "该文件为非图片格式文件，请重新上传。");
        }

        mav.addObject("course", course);
        mav.addObject("naturalSize", imageSize);
        mav.addObject("scaledSize", new ImageSize(480, 270));
        mav.addObject("pictureUrl", "tmp/" + filename);

        return mav;
    }

    @RequestMapping("/course/{id}/manage/teacher")
    public String teachersAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> course = courseService.tryManageCourse(currentUser, id);

        if ("POST".equals(request.getMethod())) {
            String[] ids = request.getParameterValues("ids[]");
            if (ids != null && ids.length > 0) {
                List<Map<String, Object>> teachers = new ArrayList<>(ids.length);
                for (String teacherId : ids) {
                    String isVisible = request.getParameter("visible_" + teacherId);
                    teachers.add(new ParamMap().add("id", teacherId)
                            .add("isVisible", EasyStringUtil.isBlank(isVisible) ? 0 : 1).toMap());
                }
                this.courseService.setCourseTeachers(currentUser, id, teachers);
                BaseControllerHelper.setFlashMessage("success", "教师设置成功！", request.getSession());
            }

            return "redirect:/course/" + id + "/manage/teacher";
        }

        List<Map<String, Object>> teacherMembers = this.courseService.findCourseTeachers(id);
        Map<String, AppUser> users = this.userService.findUsersByIds(ArrayToolkit.column(teacherMembers, "userId"));

        List<Map<String, Object>> teachers = new ArrayList<>(teacherMembers.size());
        for (Map<String, Object> member : teacherMembers) {
            String toUserId = String.valueOf(member.get("userId"));
            if (users.get(toUserId) == null) {
                continue;
            }
            teachers.add(new ParamMap()
                    .add("id", toUserId)
                    .add("nickname", users.get(toUserId).getUsername())
                    .add("avatar", new WebExtensionPack(request).getFilePath(users.get(toUserId).getSmallAvatar(),
                            "avatar.png"))
                    .add("isVisible", EasyStringUtil.isNotBlank(member.get("isVisible")) ? Boolean.TRUE : Boolean.FALSE)
                    .toMap());
        }

        model.addAttribute("teachers", "");
        model.addAttribute("course", course);
        return "/course/manage/teacher";
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


