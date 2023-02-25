package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.toolbag.ArrayToolkitOnJava8;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
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
public class TeacherController {

    private final AppUserService userService;

    public TeacherController(AppUserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/teacher")
    public String indexAction(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = new ParamMap().add("roles", "ROLE_TEACHER").add("locked", 0).toMap();
        Paginator paginator = new Paginator(request, userService.searchUserCount(conditions), 20);

        List<AppUser> teachers = userService.searchUsers(conditions,
                OrderBy.build(1).addDesc("promotedTime"),
                paginator.getOffsetCount(), paginator.getPerPageCount());

        List<Map<String, Object>> profiles = userService.findUserProfilesByIds(ArrayToolkitOnJava8.column(teachers, AppUser::getId));
        model.addAttribute("profiles", ArrayToolkit.index(profiles, "id"));
        model.addAttribute("teachers", teachers);
        model.addAttribute("paginator", paginator);
        return "/teacher/index";
    }
}
