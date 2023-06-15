package org.edunext.coursework.web.controller;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LessonLessonPluginController {

    private final CourseService courseService;
    private final AppSettingService settingService;

    public LessonLessonPluginController(CourseService courseService,
                                        AppSettingService settingService) {

        this.courseService = courseService;
        this.settingService = settingService;
    }

    @RequestMapping("/lessonplugin/lesson/list")
    public String listAction(@RequestParam Integer courseId, HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);
        this.courseService.tryTakeCourse(courseId, user);

        model.addAttribute("course", this.courseService.getCourse(courseId));
        model.addAttribute("items", this.courseService.getCourseItems(courseId));
        model.addAttribute("learnStatuses", this.courseService.getUserLearnLessonStatuses(user.getId(), courseId));

        String appEnabled = this.settingService.getSettingValue("homework.app.enabled", "0");
        if (!"0".equals(appEnabled)) {
//            List<Map<String, Object>> lessons = this.courseService.getCourseLessons(courseId);
//            Set<Object> lessonIds = ArrayToolkit.column(lessons, "id");
//            List<Map<String, Object>> homeworks = this.homeworkService.findHomeworksByCourseIdAndLessonIds(courseId, lessonIds);
//            List<Map<String, Object>> exercises = this.exerciseService.findExercisesByLessonIds(lessonIds);
//            model.addAttribute("homeworkLessonIds", ArrayToolkit.column(homeworks, "lessonId"));
//            model.addAttribute("exercisesLessonIds", ArrayToolkit.column(exercises, "lessonId"));
        }

        model.addAttribute("currentTime", System.currentTimeMillis());

        return "/lesson/plugin/lesson/list";
    }
}
