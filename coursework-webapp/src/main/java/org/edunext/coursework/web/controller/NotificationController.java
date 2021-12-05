package org.edunext.coursework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jingjianxin
 */
@Controller
public class NotificationController {

    @RequestMapping("/notification")
    public String indexPage(){
        return "/notification/index";
    }
}
