package org.edunext.coursework.web.controller.admin;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseAnnouncementService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Controller("courseAnnouncementAdminController")
public class CourseAnnouncementAdminController {

    private final CourseAnnouncementService announcementService;
    private final CourseService courseService;
    private final AppUserService userService;

    public CourseAnnouncementAdminController(CourseAnnouncementService announcementService,
                                             CourseService courseService,
                                             AppUserService userService) {

        this.announcementService = announcementService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @RequestMapping("/admin/course/announcement")
    public String indexAction(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = ParamMap.toQueryAllMap(request);

        if ("courseTitle".equals(conditions.get("keywordType"))) {
            List<Map<String, Object>> courses = this.courseService.findCoursesByLikeTitle(conditions.get("keyword"));
            Set<Object> courseIds = ArrayToolkit.column(courses, "id");
            conditions.put("courseIds", courseIds);
            if (courseIds == null || courseIds.size() == 0) {
                return "/admin/course/announcement/index";
            }
        }

        Paginator paginator = new Paginator(request, this.announcementService.searchAnnouncementCount(conditions), 20);
        List<Map<String, Object>> announcements = this.announcementService.searchAnnouncements(conditions, "created",
                paginator.getOffsetCount(),
                paginator.getPerPageCount());
        model.addAttribute("users",
                this.userService.findUsersByIds(ArrayToolkit.column(announcements, "userId")));
        model.addAttribute("courses",
                ArrayToolkit.index(this.courseService.findCoursesByIds(ArrayToolkit.column(announcements, "courseId")), "id"));
        model.addAttribute("paginator", paginator);
        model.addAttribute("announcements", announcements);
        return "/admin/course/announcement/index";
    }

    @RequestMapping("/admin/course/announcement/{id}/delete")
    @ResponseBody
    public Boolean deleteAction(@PathVariable Integer id, HttpServletRequest request) {
        this.announcementService.deleteAnnouncement(id, AppUser.getCurrentUser(request));
        return Boolean.TRUE;
    }

    @RequestMapping("/admin/course/announcement/batch_delete")
    @ResponseBody
    public Boolean batchDeleteAction(HttpServletRequest request) {
        String[] ids = request.getParameterValues("ids[]");
        if (ids != null || ids.length > 0) {
            this.announcementService.deleteAnnouncement(ids, AppUser.getCurrentUser(request));
        }
        return Boolean.TRUE;
    }
}
