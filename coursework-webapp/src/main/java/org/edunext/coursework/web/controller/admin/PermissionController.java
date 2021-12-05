package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author xulixin
 */
@Controller("adminPermissionController")
public class PermissionController {

    @GetMapping("/admin/permission")
    public String indexPage() {
        return "/admin/permission/index";
    }

    @GetMapping("/admin/permission/create")
    public String createRolePage() {
        return "/admin/permission/create-model";
    }
}
