package org.edunext.coursework.web.controller;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.EasyWebFormEditor;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import org.edunext.coursework.kernel.service.CourseMaterialService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseMaterialManageController {

    private final CourseService courseService;
    private final CourseMaterialService materialService;
    private final AppSettingService settingService;

    public CourseMaterialManageController(CourseService courseService,
                                          CourseMaterialService materialService,
                                          AppSettingService settingService) {

        this.courseService = courseService;
        this.materialService = materialService;
        this.settingService = settingService;
    }

    @RequestMapping("/course/{courseId}/manage/lesson/{lessonId}/material")
    public String indexAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                              HttpServletRequest request, Model model) {

        model.addAttribute("course", this.courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId));
        model.addAttribute("lesson", this.courseService.getCourseLesson(courseId, lessonId));
        model.addAttribute("materials", this.materialService.findLessonMaterials(lessonId, 0, 100));
        model.addAttribute("storageSetting", this.settingService.get("storage"));
        model.addAttribute("targetType", "coursematerial");
        model.addAttribute("targetId", courseId);
        return "/course/manage/material/material-modal";
    }

    @PostMapping("/course/{courseId}/manage/lesson/{lessonId}/material/upload")
    public String uploadAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                               HttpServletRequest request, Model model) {

        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> course = this.courseService.tryManageCourse(currentUser, courseId);
        Map<String, Object> lesson = this.courseService.getCourseLesson(courseId, lessonId);
        if (MapUtil.isEmpty(lesson)) {
            throw new RuntimeGoingException("课时不存在，不能进行操作！");
        }

        Map<String, Object> fields = EasyWebFormEditor.createFormEditor().bind(request).getData();

        if (EasyStringUtil.isBlank(fields.get("fileId")) && EasyStringUtil.isBlank(fields.get("link"))) {
            throw new RuntimeGoingException("没有上传文件或网络链接，不能添加资料！");
        }

        fields.put("courseId", course.get("id"));
        fields.put("lessonId", lesson.get("id"));
        AppUser.putCurrentUser(fields, currentUser);

        model.addAttribute("material", this.materialService.uploadMaterial(fields));
        return "/course/manage/material/list-item";
    }

    @RequestMapping("/course/{courseId}/manage/lesson/{lessonId}/material/{materialId}/delete")
    @ResponseBody
    public Boolean deleteAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                                @PathVariable Integer materialId, HttpServletRequest request) {

        this.courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);
        this.materialService.deleteMaterial(courseId, materialId);
        return Boolean.TRUE;
    }
}
