package org.edunext.coursework.web.controller;

import org.edunext.coursework.web.block.BlockRenderController;
import org.edunext.coursework.web.block.BlockRenderMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller
public class UserController implements BlockRenderController {

    @RequestMapping("/user/show")
    public String showUserPage() {
        return "/user/course";
    }

    @GetMapping("/user/headerBlock")
    @BlockRenderMethod
    public String showHeaderBlock() {
        return "/user/header-block";
    }
}
