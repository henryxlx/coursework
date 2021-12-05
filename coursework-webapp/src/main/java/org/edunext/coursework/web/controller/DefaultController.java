package org.edunext.coursework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller
public class DefaultController {

    @RequestMapping({"/", "/index"})
    public String index() {
        return "/default/index";
    }
}
