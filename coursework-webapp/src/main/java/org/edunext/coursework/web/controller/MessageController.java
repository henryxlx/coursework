package org.edunext.coursework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author libingquan
 */
@Controller
public class MessageController {

    @RequestMapping("/message")
    public String indexPage(){
        return "/message/index";
    }
}
