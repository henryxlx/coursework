package org.edunext.coursework.web.controller;

import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppNotificationService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseAnnouncementController {

    private final CourseService courseService;
    private final AppNotificationService notificationService;

    public CourseAnnouncementController(CourseService courseService, AppNotificationService notificationService) {
        this.courseService = courseService;
        this.notificationService = notificationService;
    }

    @GetMapping("/course/{courseId}/announcement/create")
    public String createPage(@PathVariable Integer courseId, Model model) {
        model.addAttribute("course", this.courseService.getCourse(courseId));
        model.addAttribute("announcement", new HashMap<>(0));
        return "/course/announcement-write-modal";
    }

    @PostMapping("/course/{courseId}/announcement/create")
    public Boolean createAction(@PathVariable Integer courseId, HttpServletRequest request) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> announcement = this.courseService.createAnnouncement(courseId,
                ParamMap.toQueryAllMap(request), currentUser);

        Map<String, Object> course = this.courseService.tryManageCourse(currentUser, courseId);

        if ("notify".equals(request.getParameter("notify"))) {
            Integer count = this.courseService.getCourseStudentCount(courseId);

            List<Map<String, Object>> members = this.courseService.findCourseStudents(courseId, 0, count);

            String courseUrl = request.getContextPath() + "/course/" + courseId;
            for (Map<String, Object> member : members) {
                this.notificationService.notify(ValueParser.toInteger(member.get("userId")), "default",
                        String.format("【课程公告】你正在学习的<a href='%s' target='_blank'>%s</a>发布了一个新的公告，<a href='%s' target='_blank'>快去看看吧</a>",
                                courseUrl, course.get("title"), courseUrl));
            }
        }

        return Boolean.TRUE;
    }
}
