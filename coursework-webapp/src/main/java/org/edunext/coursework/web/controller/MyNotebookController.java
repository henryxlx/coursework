package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class MyNotebookController {

    private final CourseService courseService;

    public MyNotebookController(CourseService courseService) {
        this.courseService = courseService;
    }

    @RequestMapping("/my/notebooks")
    public String indexAction(HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);

        Map<String, Object> conditions = new ParamMap().add("userId", user.getId())
                .add("noteNumGreaterThan", 0.1).toMap();

        Paginator paginator = new Paginator(request, this.courseService.searchMemberCount(conditions), 10);

        List<Map<String, Object>> courseMembers = this.courseService.searchMember(conditions,
                paginator.getOffsetCount(), paginator.getPerPageCount());

        model.addAttribute("courses",
                this.courseService.findCoursesByIds(ArrayToolkit.column(courseMembers, "courseId")));
        model.addAttribute("courseMembers", courseMembers);
        model.addAttribute("paginator", paginator);
        return "/my/notebook/index";
    }
}
