package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jingjianxin
 */
@Controller
public class CourseNoteController {

    @RequestMapping("/admin/course/note")
    public String indexPage() {
        return "/admin/course/note/index";
    }
}
