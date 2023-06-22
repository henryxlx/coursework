package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyWebFormEditor;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppNotificationService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final AppUserService userService;

    public CourseAnnouncementController(CourseService courseService,
                                        AppNotificationService notificationService,
                                        AppUserService userService) {

        this.courseService = courseService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/course/{courseId}/announcement/create")
    public String createPage(@PathVariable Integer courseId, Model model) {
        model.addAttribute("course", this.courseService.getCourse(courseId));
        model.addAttribute("announcement", new HashMap<>(0));
        return "/course/announcement-write-modal";
    }

    @PostMapping("/course/{courseId}/announcement/create")
    @ResponseBody
    public Boolean createAction(@PathVariable Integer courseId, HttpServletRequest request) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> announcement = this.courseService.createAnnouncement(courseId,
                ParamMap.toQueryAllMap(request), currentUser);

        Map<String, Object> course = this.courseService.tryManageCourse(currentUser, courseId);

        if ("notify".equals(request.getParameter("notify"))) {
            Integer count = this.courseService.getCourseStudentCount(courseId);
            // fixed. sql injection violation, dbType mysql, druid-version 1.2.8, limit row 0
            if (count == 0) {
                count = 10;
            }

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

    @RequestMapping("/course/{courseId}/announcement/{id}/delete")
    @ResponseBody
    public Boolean deleteAction(@PathVariable Integer courseId, @PathVariable Integer id, HttpServletRequest request) {
        this.courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);
        this.courseService.deleteCourseAnnouncement(courseId, id);
        return Boolean.TRUE;
    }

    @RequestMapping("/course/{courseId}/announcement/{id}")
    public String showAction(@PathVariable Integer courseId, @PathVariable Integer id,
                             HttpServletRequest request, Model model) {

        AppUser user = AppUser.getCurrentUser(request);
        this.courseService.tryTakeCourse(courseId, user);
        model.addAttribute("announcement", this.courseService.getCourseAnnouncement(courseId, id));
        model.addAttribute("course", this.courseService.getCourse(courseId));
        model.addAttribute("canManage", this.courseService.canManageCourse(courseId, user.getId()));
        return "/course/announcement-show-modal";
    }

    @RequestMapping("/course/{courseId}/announcements")
    public String showAllAction(@PathVariable Integer courseId, HttpServletRequest request, Model model) {
        List<Map<String, Object>> announcements = this.courseService.findAnnouncements(courseId, 0, 10000);
        model.addAttribute("users",
                this.userService.findUsersByIds(ArrayToolkit.column(announcements, "userId")));
        model.addAttribute("announcements", announcements);
        return "/course/announcement-show-all-modal";
    }

    @GetMapping("/course/{courseId}/announcement/{id}/update")
    public String updatePage(@PathVariable Integer courseId, @PathVariable Integer id,
                             HttpServletRequest request, Model model) {

        Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);

        Map<String, Object> announcement = this.courseService.getCourseAnnouncement(courseId, id);
        if (MapUtil.isEmpty(announcement)) {
            throw new RuntimeGoingException("课程公告(#" + id + ")不存在。");
        }

        model.addAttribute("announcement", announcement);
        model.addAttribute("course", course);
        return "/course/announcement-write-modal";
    }

    @PostMapping("/course/{courseId}/announcement/{id}/update")
    @ResponseBody
    public Boolean updateAction(@PathVariable Integer courseId, @PathVariable Integer id,
                                HttpServletRequest request) {

        Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);

        Map<String, Object> announcement = this.courseService.getCourseAnnouncement(courseId, id);
        if (MapUtil.isEmpty(announcement)) {
            throw new RuntimeGoingException("课程公告(#" + id + ")不存在。");
        }

        this.courseService.updateAnnouncement(courseId, id,
                EasyWebFormEditor.createFormEditor().bind(request).getData());
        return Boolean.TRUE;
    }
}
