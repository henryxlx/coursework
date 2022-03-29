package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jingjianxin
 */
@Controller("admin/review")
public class ReviewController {
    @RequestMapping("admin/review")
    public String reviewPage(){
        return "admin/review/index";}

}
