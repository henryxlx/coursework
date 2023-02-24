package org.edunext.coursework.web.controller.admin;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseExtendUserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author jingjianxin
 */
@Controller("adminTeacherController")
public class TeacherController {

    private final AppUserService userService;

    private final CourseExtendUserServiceImpl extendUserService;

    public TeacherController(AppUserService userService, CourseExtendUserServiceImpl extendUserService) {
        this.userService = userService;
        this.extendUserService = extendUserService;
    }

    @RequestMapping("/admin/teacher")
    public String indexPage(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = new ParamMap().add("roles", "ROLE_TEACHER").toMap();

        Paginator paginator = new Paginator(request, userService.searchUserCount(conditions), 20);

        model.addAttribute("users", userService.searchUsers(conditions, OrderBy.build(1).addDesc("promotedTime"),
                paginator.getOffsetCount(),
                paginator.getPerPageCount()));

        model.addAttribute("paginator", paginator);

        return "/admin/teacher/index";
    }

    @RequestMapping("/admin/teacher/{id}/promote")
    @ResponseBody
    public Boolean promoteAction(HttpServletRequest request, @PathVariable Integer id) {
        extendUserService.promoteUser(AppUser.getCurrentUser(request), id);
        return Boolean.TRUE;
    }

    @RequestMapping("/admin/teacher/{id}/promote/cancel")
    @ResponseBody
    public Boolean promoteCancelAction(HttpServletRequest request, @PathVariable Integer id) {
        extendUserService.cancelPromoteUser(AppUser.getCurrentUser(request), id);
        return Boolean.TRUE;
    }
}
