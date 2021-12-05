package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller("adminNavigationController")
public class NavigationController {

    @RequestMapping("/admin/navigation")
    public String indexPage() {
        return "/admin/navigation/index";
    }
}
