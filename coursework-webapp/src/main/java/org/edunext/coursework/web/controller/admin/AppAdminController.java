package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller("appAdminController")
public class AppAdminController {

    @RequestMapping("/admin/app/installed")
    public String appInstalledPage() {
        return "/admin/app/installed";
    }
}
