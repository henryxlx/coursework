package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller("frontCourseLessonController")
public class CourseLessonController {

    private final CourseService courseService;
    private final AppSettingService settingService;
    private final UserAccessControlService userAccessControlService;

    public CourseLessonController(CourseService courseService,
                                  AppSettingService settingService,
                                  UserAccessControlService userAccessControlService) {

        this.courseService = courseService;
        this.settingService = settingService;
        this.userAccessControlService = userAccessControlService;
    }

    @RequestMapping("/course/{courseId}/lesson/{lessonId}/preview")
    public String previewAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                                HttpServletRequest request, Model model) {

        Map<String, Object> course = this.courseService.getCourse(courseId);
        Map<String, Object> lesson = this.courseService.getCourseLesson(courseId, lessonId);
        AppUser user = AppUser.getCurrentUser(request);

        if (MapUtil.isEmpty(lesson)) {
            throw new RuntimeGoingException("课时信息未找到!");
        }

        if ("closed".equals(course.get("status"))) {
            model.addAttribute("course", course);
            return "/course/lesson/preview-notice-modal";
        }

        if ("1".equals(lesson.get("free"))) {
            String allowAnonymousPreview = settingService.getSettingValue("course.allowAnonymousPreview", "1");
            if (!"1".equals(allowAnonymousPreview) && !userAccessControlService.isLoggedIn()) {
                throw new RuntimeGoingException("由于系统设置未登录用户无法访问免费课时！");
            }
        } else {
            if (!userAccessControlService.isLoggedIn()) {
                throw new RuntimeGoingException("未登录用户无法访问课时！");
            }
//            model.addAttribute("id", courseId);
//            model.addAttribute("preview", Boolean.TRUE);
//            return "forward:/course/order/buy";
        }

        int hasVideoWatermarkEmbedded = 0;
        if ("video".equals(lesson.get("type")) && "self".equals(lesson.get("mediaSource"))) {
            // not finished.
        } else if ("youku".equals(lesson.get("mediaSource"))) {
            // not finished.
        }

        model.addAttribute("user", user);
        model.addAttribute("course", course);
        model.addAttribute("lesson", lesson);
        model.addAttribute("hasVideoWatermarkEmbedded", hasVideoWatermarkEmbedded);
        model.addAttribute("hlsUrl", "");
        return "/course/lesson/preview-modal";
    }
}
