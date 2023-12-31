package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.toolbag.FileToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.image.ImageSize;
import com.jetwinner.webfast.image.ImageUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.FastAppConst;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import com.jetwinner.webfast.module.bigapp.service.AppTagService;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import com.jetwinner.webfast.mvc.extension.WebExtensionPack;
import org.edunext.coursework.kernel.service.CourseNoteService;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.CourseThreadService;
import org.edunext.coursework.kernel.service.TestPaperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    private final CourseNoteService noteService;
    private final CourseThreadService threadService;
    private final TestPaperService testPaperService;
    private final AppCategoryService categoryService;
    private final AppSettingService settingService;
    private final FastAppConst appConst;
    private final AppTagService tagService;
    private final AppUserService userService;

    public CourseManageController(CourseService courseService,
                                  CourseNoteService noteService,
                                  CourseThreadService threadService,
                                  TestPaperService testPaperService,
                                  AppCategoryService categoryService,
                                  AppSettingService settingService,
                                  FastAppConst appConst,
                                  AppTagService tagService,
                                  AppUserService userService) {

        this.courseService = courseService;
        this.noteService = noteService;
        this.threadService = threadService;
        this.testPaperService = testPaperService;
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

        WebExtensionPack webExtensionPack = new WebExtensionPack(request);
        List<Map<String, Object>> teachers = new ArrayList<>(teacherMembers.size());
        for (Map<String, Object> member : teacherMembers) {
            String toUserId = String.valueOf(member.get("userId"));
            if (users.get(toUserId) == null) {
                continue;
            }
            teachers.add(new ParamMap()
                    .add("id", toUserId)
                    .add("username", users.get(toUserId).getUsername())
                    .add("avatar", webExtensionPack.getFilePath(users.get(toUserId).getSmallAvatar(),
                            "avatar.png"))
                    .add("isVisible", EasyStringUtil.isNotBlank(member.get("isVisible")) ? Boolean.TRUE : Boolean.FALSE)
                    .toMap());
        }

        model.addAttribute("teachers", teachers);
        model.addAttribute("course", course);
        return "/course/manage/teacher";
    }

    @RequestMapping("/course/{id}/manage/teachersMatch")
    @ResponseBody
    public List<Map<String, Object>> teachersMatchAction(@PathVariable Integer id, HttpServletRequest request) {
        String likeString = request.getParameter("q");
        List<AppUser> users = this.userService.searchUsers(
                new ParamMap().add("username", likeString).add("roles", "ROLE_TEACHER").toMap(),
                OrderBy.build(1).addDesc("createdTime"), 0, 10);

        List<Map<String, Object>> teachers = new ArrayList<>(users.size());
        WebExtensionPack webExtensionPack = new WebExtensionPack(request);
        users.forEach(user -> {
            teachers.add(new ParamMap()
                    .add("id", user.getId())
                    .add("username", user.getUsername())
                    .add("avatar", webExtensionPack.getFilePath(user.getSmallAvatar(), "avatar.png"))
                    .add("isVisible", 1).toMap());
        });
        return teachers;
    }

    @RequestMapping("/course/{id}/manage/publish")
    @ResponseBody
    public Boolean publishAction(@PathVariable Integer id, HttpServletRequest request) {
        this.courseService.publishCourse(AppUser.getCurrentUser(request), id);
        return Boolean.TRUE;
    }


    @RequestMapping("/course/{id}/manage/data")
    public String dataAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);

        Integer isLearnedNum = this.courseService.searchMemberCount(FastHashMap.build(2)
                .add("isLearned", 1).add("courseId", id).toMap());

        Integer learnTime = this.courseService.searchLearnTime(FastHashMap.build(1).add("courseId", id).toMap());
        int studentNum = ValueParser.parseInt(course.get("studentNum"));
        learnTime = studentNum == 0 ? 0 : (int) (1.0 * learnTime / studentNum);

        Integer noteCount = this.noteService.searchNoteCount(FastHashMap.build(1).add("courseId", id).toMap());

        Integer questionCount = this.threadService.searchThreadCount(FastHashMap.build(2)
                .add("courseId", id).add("type", "question").toMap());

        List<Map<String, Object>> lessons = this.courseService.searchLessons(FastHashMap.build(1).add("courseId", id).toMap(),
                OrderBy.build(1).addDesc("createdTime"), 0, 1000);

        for (Map<String, Object> value : lessons) {
            int lessonLearnedNum = this.courseService.findLearnsCountByLessonId(ValueParser.toInteger(value.get("id")));

            int finishedNum = this.courseService.searchLearnCount(FastHashMap.build(2)
                    .add("status", "finished").add("lessonId", value.get("id")).toMap());

            int lessonLearnTime = this.courseService.searchLearnTime(FastHashMap.build(1).add("lessonId", value.get("id")).toMap());
            lessonLearnTime = lessonLearnedNum == 0 ? 0 : (int) (1.0 * lessonLearnTime / lessonLearnedNum);

            int lessonWatchTime = this.courseService.searchWatchTime(FastHashMap.build(1).add("lessonId", value.get("id")).toMap());
            lessonWatchTime = lessonWatchTime == 0 ? 0 : (int) (1.0 * lessonWatchTime / lessonLearnedNum);

            value.put("LearnedNum", lessonLearnedNum);
            value.put("length", (int) (1.0 * ValueParser.parseInt(value.get("length")) / 60));
            value.put("finishedNum", finishedNum);
            value.put("learnTime", lessonLearnTime);
            value.put("watchTime", lessonWatchTime);

            if ("testpaper".equals(value.get("type"))) {
                Object paperId = value.get("mediaId");
                int score = this.testPaperService.searchTestpapersScore(FastHashMap.build(1).add("testId", paperId).toMap());
                int paperNum = this.testPaperService.searchTestpaperResultsCount(FastHashMap.build(1).add("testId", paperId).toMap());
                value.put("score", finishedNum == 0 ? 0 : (int) (1.0 * score / paperNum));
            }
        }

        model.addAttribute("isLearnedNum", isLearnedNum);
        model.addAttribute("learnTime", learnTime);
        model.addAttribute("noteCount", noteCount);
        model.addAttribute("questionCount", questionCount);
        model.addAttribute("lessons", lessons);
        model.addAttribute("course", course);
        return "/course/manage/learning-data";
    }
}


