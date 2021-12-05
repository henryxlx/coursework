package org.edunext.coursework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PasswordResetController {

    @GetMapping("/password/reset")
    public String indexPage() {
        return "/password-reset/index";
    }

}
