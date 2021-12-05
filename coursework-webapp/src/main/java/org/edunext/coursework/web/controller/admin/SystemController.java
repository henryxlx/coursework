package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller("adminSystemController")
public class SystemController {

    @RequestMapping("/admin/setting/site")
    public String sitePage() {
        return "/admin/system/site";
    }

}
