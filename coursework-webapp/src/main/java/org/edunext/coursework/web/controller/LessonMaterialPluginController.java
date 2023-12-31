package org.edunext.coursework.web.controller;

import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import org.edunext.coursework.kernel.service.CourseMaterialService;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.UploadFileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class LessonMaterialPluginController {

    private final CourseService courseService;
    private final CourseMaterialService materialService;
    private final UploadFileService uploadFileService;

    public LessonMaterialPluginController(CourseService courseService,
                                          CourseMaterialService materialService,
                                          UploadFileService uploadFileService) {

        this.courseService = courseService;
        this.materialService = materialService;
        this.uploadFileService = uploadFileService;
    }

    @RequestMapping("/lessonplugin/material/init")
    public String initAction(Integer courseId, Integer lessonId, HttpServletRequest request, Model model) {
        this.courseService.tryTakeCourse(courseId, AppUser.getCurrentUser(request));
        Map<String, Object> lesson = this.courseService.getCourseLesson(courseId, lessonId);

        Map<String, Object> file = null;
        if (MapUtil.isNotEmpty(lesson) && ValueParser.parseInt(lesson.get("mediaId")) > 0) {
            file = this.uploadFileService.getFile(lesson.get("mediaId"));
        }
        model.addAttribute("file", file);
        model.addAttribute("materials", this.materialService.findLessonMaterials(lessonId, 0, 100));
        model.addAttribute("lesson", lesson);
        model.addAttribute("course", courseService.getCourse(courseId));

        return "/lesson/plugin/material/index";
    }

}