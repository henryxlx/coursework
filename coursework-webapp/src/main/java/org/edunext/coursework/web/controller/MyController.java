package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller
public class MyController {

    private final UserAccessControlService userAccessControlService;

    public MyController(UserAccessControlService userAccessControlService) {
        this.userAccessControlService = userAccessControlService;
    }

    @RequestMapping("/my")
    public String myPage() {
        if (userAccessControlService.hasRole("ROLE_TEACHER")) {
            return "redirect:/my/teaching/courses";
        } else {
            return "redirect:/my/courses/learning";
        }
    }
}
