package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseNoteService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class MyNotebookController {

    private final CourseService courseService;
    private final CourseNoteService noteService;

    public MyNotebookController(CourseService courseService, CourseNoteService noteService) {
        this.courseService = courseService;
        this.noteService = noteService;
    }

    @RequestMapping("/my/notebooks")
    public String indexAction(HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);

        Map<String, Object> conditions = new ParamMap().add("userId", user.getId())
                .add("noteNumGreaterThan", 0.1).toMap();

        Paginator paginator = new Paginator(request, this.courseService.searchMemberCount(conditions), 10);

        List<Map<String, Object>> courseMembers = this.courseService.searchMember(conditions,
                paginator.getOffsetCount(), paginator.getPerPageCount());

        model.addAttribute("courses",
                ArrayToolkit.index(this.courseService.findCoursesByIds(ArrayToolkit.column(courseMembers, "courseId")), "id"));
        model.addAttribute("courseMembers", courseMembers);
        model.addAttribute("paginator", paginator);
        return "/my/notebook/index";
    }

    @RequestMapping("/my/notebook/{courseId}")
    public String showAction(@PathVariable Integer courseId, HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);

        Map<String, Object> course = this.courseService.getCourse(courseId);
        Map<String, Map<String, Object>> lessons =
                ArrayToolkit.index(this.courseService.getCourseLessons(courseId), "id");
        model.addAttribute("lessons", lessons);

        List<Map<String, Object>> notes = this.noteService.findUserCourseNotes(user.getId(),
                ValueParser.toInteger(course.get("id")));
        notes.forEach(note -> note.put("lessonNumber", EasyStringUtil.isBlank(note.get("lessonId")) ? 0 :
                lessons.get("" + note.get("lessonId")).get("number")));

        notes.sort((note1, note2) -> {
            int num1 = ValueParser.parseInt(note1.get("lessonNumber"));
            int num2 = ValueParser.parseInt(note2.get("lessonNumber"));
            if (num1 == 0) {
                return 1;
            }
            if (num2 == 0) {
                return -1;
            }
            return num1 - num2;
        });
        model.addAttribute("notes", notes);
        model.addAttribute("course", course);
        return "/my/notebook/show";
    }

    @RequestMapping("/my/note/{id}/delete")
    @ResponseBody
    public Boolean noteDeleteAction(@PathVariable Integer id, HttpServletRequest request) {
        this.noteService.deleteNote(id, AppUser.getCurrentUser(request));
        return Boolean.TRUE;
    }
}
