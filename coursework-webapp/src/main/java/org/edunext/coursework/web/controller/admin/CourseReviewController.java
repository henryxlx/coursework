package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jingjianxin
 */
@Controller("adminReviewController")
@RequestMapping("/admin/course/review")
public class CourseReviewController {

    private static final String VIEW_PREFIX = "/admin/course/review/";

    @RequestMapping({"", "/"})
    public String indexPage() {
        return VIEW_PREFIX + "index";
    }

}
