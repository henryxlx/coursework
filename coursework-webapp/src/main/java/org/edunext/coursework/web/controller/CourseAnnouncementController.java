package org.edunext.coursework.web.controller;

import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseAnnouncementController {

    private final CourseService courseService;

    public CourseAnnouncementController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course/{courseId}/announcement/create")
    public String createPage(@PathVariable Integer courseId, Model model) {
        model.addAttribute("course", this.courseService.getCourse(courseId));

        Map<String, Object> fieldMap = new HashMap<>(2);
        fieldMap.put("id", "");
        fieldMap.put("content", "");
        model.addAttribute("announcement", fieldMap);

        return "/course/announcement-write-modal";
    }
}
