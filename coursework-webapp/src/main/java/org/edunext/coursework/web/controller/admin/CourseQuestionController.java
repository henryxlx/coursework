package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller
public class CourseQuestionController {

    @RequestMapping("/admin/course/question")
    public String indexPage() {
        return "/admin/course/question/index";
    }

}
