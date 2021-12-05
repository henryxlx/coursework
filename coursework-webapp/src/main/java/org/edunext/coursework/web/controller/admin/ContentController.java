package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller("adminContentController")
public class ContentController {

    @RequestMapping("/admin/content")
    public String indexPage() {
        return "/admin/content/index";
    }
}
