package org.edunext.coursework.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xulixin
 */
@Controller("adminCourseAnalysisController")
public class CourseAnalysisController {

    @RequestMapping("/admin/operation/analysis/course-sum/{tab}")
    public String courseSumAction(@PathVariable String tab, HttpServletRequest request, Model model) {
        return "/admin/operation/analysis/course-sum";
    }

    @RequestMapping("/admin/operation/analysis/course/{tab}")
    public String courseAction(@PathVariable String tab, HttpServletRequest request, Model model) {
        return "/admin/operation/analysis/course";
    }

    @RequestMapping("/admin/operation/analysis/lesson/{tab}")
    public String lessonAction(@PathVariable String tab, HttpServletRequest request, Model model) {
        return "/admin/operation/analysis/lesson";
    }

    @RequestMapping("/admin/operation/analysis/join-lesson/{tab}")
    public String joinLessonAction(@PathVariable String tab, HttpServletRequest request, Model model) {
        return "/admin/operation/analysis/join-lesson";
    }

    @RequestMapping("/admin/operation/analysis/finished-lesson/{tab}")
    public String finishedLessonAction(@PathVariable String tab, HttpServletRequest request, Model model) {
        return "/admin/operation/analysis/finished-lesson";
    }

    @RequestMapping("/admin/operation/analysis/video-view/{tab}")
    public String viewedVideoAction(@PathVariable String tab, HttpServletRequest request, Model model) {
        return "/admin/operation/analysis/video-view";
    }

    @RequestMapping("/admin/operation/analysis/video-view-cloud/{tab}")
    public String videoViewedCloudAction(@PathVariable String tab, HttpServletRequest request, Model model) {
        return "/admin/operation/analysis/video-view-cloud";
    }

    @RequestMapping("/admin/operation/analysis/video-view-local/{tab}")
    public String videoViewedLocalAction(@PathVariable String tab, HttpServletRequest request, Model model) {
        return "/admin/operation/analysis/video-view-local";
    }

    @RequestMapping("/admin/operation/analysis/video-view-net/{tab}")
    public String videoViewedNetAction(@PathVariable String tab, HttpServletRequest request, Model model) {
        return "/admin/operation/analysis/video-view-net";
    }
}
