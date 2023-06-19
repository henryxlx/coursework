package org.edunext.coursework.web.controller;

import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.ListUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.mvc.block.BlockRenderController;
import com.jetwinner.webfast.mvc.block.BlockRenderMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author xulixin
 */
@Controller("courseWorkExtendDefaultController")
public class DefaultController implements BlockRenderController {

    private final AppUserService userService;

    public DefaultController(AppUserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/default/promotedTeacherBlock")
    @BlockRenderMethod
    public String promotedTeacherBlockAction(Model model) {
        AppUser teacher = null;
        List<AppUser> teachers = this.findLatestPromotedTeacher(0, 1);
        if (ListUtil.isNotEmpty(teachers)) {
            teacher = teachers.get(0);
            model.addAttribute("teacherProfile", this.userService.getUserProfile(teacher.getId()));
        }

        if (teacher != null && teacher.getLocked() != 0) {
            teacher = null;
        }
        model.addAttribute("teacher", teacher);

        return "/default/promoted-teacher-block";
    }

    private List<AppUser> findLatestPromotedTeacher(Integer start, Integer limit) {
        return this.userService.searchUsers(FastHashMap.build(2)
                        .add("roles", "ROLE_TEACHER").add("promoted", 1).toMap(),
                OrderBy.build(1).addDesc("promotedTime"), start, limit);
    }


}
