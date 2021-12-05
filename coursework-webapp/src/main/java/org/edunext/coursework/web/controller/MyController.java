package org.edunext.coursework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller
public class MyController {

    @RequestMapping("/my")
    public String myPage() {
        return "/my/index";
    }
}
