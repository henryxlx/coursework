package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jingjianxin
 */
@Controller
public class NoteController {

    @RequestMapping("/admin/note")
    public String indexPage() {
        return "/admin/note/index";
    }
}
