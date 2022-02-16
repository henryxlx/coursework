package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TagController {

    @RequestMapping("/admin/tag")
    public String indexPage() {
        return "/admin/tag/index";
    }
}
