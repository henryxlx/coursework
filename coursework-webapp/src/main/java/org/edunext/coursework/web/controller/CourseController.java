package org.edunext.coursework.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xulixin
 */
@Controller
public class CourseController {

    @RequestMapping("/course/archive")
    public String archivePage() {
        return "/course/archive";
    }
}
