package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller("adminAnalysisController")
public class AnalysisController {

    @RequestMapping("/admin/operation/analysis/register/{tab}")
    public String registerPage(@PathVariable String tab) {
        return "/admin/operation/analysis/register";
    }


}
