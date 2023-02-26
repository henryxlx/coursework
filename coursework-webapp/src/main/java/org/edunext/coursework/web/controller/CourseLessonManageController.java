package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Controller
public class CourseLessonManageController {

    private final CourseService courseService;
    private final AppSettingService settingService;

    public CourseLessonManageController(CourseService courseService,
                                        AppSettingService settingService) {

        this.courseService = courseService;
        this.settingService = settingService;
    }

    @RequestMapping("/course/{id}/manage/lesson")
    public String indexAction(HttpServletRequest request, @PathVariable Integer id, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        Map<String, Map<String, Object>> courseItems = courseService.getCourseItems(course.get("id"));

        Set<Object> lessonIds = ArrayToolkit.column(courseItems.values(), "id");

        if ("1".equals(settingService.getSettingValue("homework.enabled"))) {
//            model.addAttribute("exercises", "Homework.ExerciseService.findExercisesByLessonIds(lessonIds);
//            model.addAttribute("homeworks", "Homework.HomeworkService.findHomeworksByCourseIdAndLessonIds(course.get("id"), lessonIds);
        }

        Map<String, Set<Object>> mediaMap = new HashMap<>();
        for (Map<String, Object> item : courseItems.values()) {
            if (!"lesson".equals(item.get("itemType"))) {
                continue;
            }

            if (EasyStringUtil.isBlank(item.get("mediaId"))) {
                continue;
            }

            mediaMap.putIfAbsent(String.valueOf(item.get("mediaId")), new HashSet<>()).add(item.get("id"));
        }

        Set<String> mediaIds = mediaMap.keySet();

/*
        List<Map<String, Object>> files = uploadFileService.findFilesByIds(mediaIds);
        for (Map<String, Object> file : files) {
            lessonIds = mediaMap.get(file.get("id"));
            for (Object lessonId : lessonIds) {
                courseItems.get("lesson-" + lessonId).put("mediaStatus", file.get("convertStatus"));
            }
        }
        model.addAttribute("files", ArrayToolkit.index(files, "id"));
*/
        model.addAttribute("course", course);
        model.addAttribute("items", courseItems);
        model.addAttribute("default", settingService.get("default"));
        return "/course/manage/lesson/index";
    }

}
