package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.JsonUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseMaterialService;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.UploadFileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseFileManageController {

    private final CourseService courseService;
    private final CourseMaterialService materialService;
    private final UploadFileService uploadFileService;
    private final AppUserService userService;
    private final AppSettingService settingService;

    public CourseFileManageController(CourseService courseService,
                                      CourseMaterialService materialService,
                                      UploadFileService uploadFileService,
                                      AppUserService userService,
                                      AppSettingService settingService) {

        this.courseService = courseService;
        this.materialService = materialService;
        this.uploadFileService = uploadFileService;
        this.userService = userService;
        this.settingService = settingService;
    }

    @RequestMapping("/course/{id}/manage/file")
    public String fileAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);

        String type = request.getParameter("type");
        type = ArrayUtil.inArray(type, "courselesson", "coursematerial") ? type : "courselesson";

        Map<String, Object> conditions = new ParamMap()
                .add("targetType", type)
                .add("targetId", course.get("id")).toMap();

        Paginator paginator = new Paginator(request, uploadFileService.searchFileCount(conditions), 20);

        List<Map<String, Object>> files = uploadFileService.searchFiles(conditions, "latestCreated",
                paginator.getOffsetCount(), paginator.getPerPageCount());

        for (Map<String, Object> file : files) {
            file.put("metas2", EasyStringUtil.isNotBlank(file.get("metas2")) ?
                    JsonUtil.jsonDecode(file.get("metas2"), Map.class) : null);

            file.put("convertParams", EasyStringUtil.isNotBlank(file.get("convertParams")) ?
                    JsonUtil.jsonDecode(file.get("convertParams"), Map.class) : null);

            int useNum= courseService.searchLessonCount(new ParamMap().add("mediaId", file.get("id")).toMap());

            int manageFilesUseNum = materialService.getMaterialCountByFileId(file.get("id"));

            if ("coursematerial".equals(file.get("targetType"))) {
                file.put("useNum", manageFilesUseNum);
            } else {
                file.put("useNum", useNum);
            }
        }

        model.addAttribute("type", type);
        model.addAttribute("course", course);
        model.addAttribute("courseLessons", files);
        model.addAttribute("users", userService.findUsersByIds(ArrayToolkit.column(files, "updatedUserId")));
        model.addAttribute("paginator", paginator);
        model.addAttribute("now", System.currentTimeMillis());
        model.addAttribute("storageSetting", settingService.get("storage"));
        return "/course/manage/file/index";
    }

    @RequestMapping("/course/{id}/manage/upload/course/file/{targetType}")
    public String uploadCourseFilesAction(@PathVariable Integer id, @PathVariable String targetType,
                                          HttpServletRequest request, Model model) {

        Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course", course);
        model.addAttribute("storageSetting", this.settingService.get("storage"));
        model.addAttribute("targetType", targetType);
        model.addAttribute("targetId", id);
        return "/course/manage/file/modal-upload-course-file";
    }
}