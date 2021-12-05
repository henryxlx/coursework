package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller("adminBlockController")
public class BlockController {

    @RequestMapping("/admin/block")
    public String indexPage() {
        return "/admin/block/index";
    }
}
