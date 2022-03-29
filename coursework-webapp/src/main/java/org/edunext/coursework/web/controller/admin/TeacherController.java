package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jingjianxin
 */
@Controller("adminTeacherController")
public class TeacherController {
    @RequestMapping("/admin/teacher")
    public String indexPage() {
        return "/admin/teacher/index";
    }
}
