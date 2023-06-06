package org.edunext.coursework.web.controller;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseStudentManageController {

    private final CourseService courseService;

    public CourseStudentManageController(CourseService courseService) {
        this.courseService = courseService;
    }

    @RequestMapping("/course/{id}/manage/student")
    public String indexAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course", course);
        return "/course/manage/student/index";
    }

    @RequestMapping("/course/{id}/manage/student/create")
    public String createAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course", course);
        return "/course/manage/student/create-modal";
    }

    @RequestMapping("/course/{id}/manage/student/export")
    public void exportCsvAction(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        String str = "用户名,Email,加入学习时间,学习进度,姓名,性别,QQ号,微信号,手机号,公司,职业,头衔";
        String filename = String.format("course-%s-students-(%s).csv", id, System.currentTimeMillis());
        response.setHeader("Content-type", "text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setHeader("Content-length", String.valueOf(str.length()));
        try {
            response.getOutputStream().print(str);
        } catch (IOException e) {
            throw new RuntimeGoingException("Export student csv error: " + e.getMessage());
        }
    }
}
