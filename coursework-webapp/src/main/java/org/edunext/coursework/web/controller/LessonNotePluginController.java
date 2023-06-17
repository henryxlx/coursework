package org.edunext.coursework.web.controller;

import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseNoteService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class LessonNotePluginController {

    private final CourseService courseService;
    private final CourseNoteService courseNoteService;

    public LessonNotePluginController(CourseService courseService, CourseNoteService courseNoteService) {
        this.courseService = courseService;
        this.courseNoteService = courseNoteService;
    }

    @RequestMapping("/lessonplugin/note/init")
    public String initAction(HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);

        Map<String, Object> course = this.courseService.getCourse(ValueParser.toInteger(request.getParameter("courseId")));
        Map<String, Object> lesson = new ParamMap()
                .add("id", ValueParser.toInteger(request.getParameter("lessonId")))
                .add("courseId", course.get("id")).toMap();
        Map<String, Object> note = this.courseNoteService.getUserLessonNote(currentUser.getId(),
                ValueParser.toInteger(lesson.get("id")));
        model.addAttribute("courseId", course.get("id"));
        model.addAttribute("lessonId", lesson.get("id"));
        if (MapUtil.isNotEmpty(note)) {
            model.addAttribute("content", note.get("content"));
            model.addAttribute("id", note.get("id"));
        }
        return "/lesson/plugin/note/index";
    }

    @RequestMapping("/lessonplugin/note/save")
    @ResponseBody
    public Boolean saveAction(HttpServletRequest request) {
        if ("POST".equals(request.getMethod())) {
            Map<String, Object> note = ParamMap.toQueryAllMap(request);
            note.put("content", note.get("note[content]"));
            this.courseNoteService.saveNote(note, AppUser.getCurrentUser(request));
        }
        return Boolean.FALSE;
    }

}