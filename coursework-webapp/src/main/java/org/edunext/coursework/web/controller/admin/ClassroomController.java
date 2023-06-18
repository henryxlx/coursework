package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller
public class ClassroomController {

    @RequestMapping("/admin/classroom")
    public String indexAction() {
        return "/admin/classroom/index";
    }
}
