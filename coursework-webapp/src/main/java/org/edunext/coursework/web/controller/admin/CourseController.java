package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminCourseController")
public class CourseController {

    @RequestMapping("/admin/course")
    public String indexPage() {
        return "/admin/course/index";
    }
}
