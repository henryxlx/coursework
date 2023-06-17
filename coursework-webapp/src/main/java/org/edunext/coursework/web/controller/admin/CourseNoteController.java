package org.edunext.coursework.web.controller.admin;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppUserService;
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
import java.util.Set;

/**
 * @author jingjianxin
 */
@Controller
public class CourseNoteController {

    private final CourseNoteService noteService;
    private final CourseService courseService;
    private final AppUserService userService;

    public CourseNoteController(CourseNoteService noteService,
                                CourseService courseService,
                                AppUserService userService) {

        this.noteService = noteService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @RequestMapping("/admin/course/note")
    public String indexAction(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = ParamMap.toQueryAllMap(request);

        if ("courseTitle".equals(conditions.get("keywordType"))) {
            List<Map<String, Object>> courses = this.courseService.findCoursesByLikeTitle(conditions.get("keyword"));
            Set<Object> courseIds = ArrayToolkit.column(courses, "id");
            conditions.put("courseIds", courseIds);
            if (courseIds == null || courseIds.size() == 0) {
                return "/admin/course/note/index";
            }
        }

        Paginator paginator = new Paginator(request, this.noteService.searchNoteCount(conditions), 20);
        List<Map<String, Object>> notes = this.noteService.searchNotes(conditions, "created",
                paginator.getOffsetCount(),
                paginator.getPerPageCount());
        model.addAttribute("users",
                this.userService.findUsersByIds(ArrayToolkit.column(notes, "userId")));
        model.addAttribute("courses",
                ArrayToolkit.index(this.courseService.findCoursesByIds(ArrayToolkit.column(notes, "courseId")), "id"));
        model.addAttribute("lessons",
                ArrayToolkit.index(this.courseService.findLessonsByIds(ArrayToolkit.column(notes, "lessonId")), "id"));
        model.addAttribute("paginator", paginator);
        model.addAttribute("notes", notes);
        return "/admin/course/note/index";
    }

    @RequestMapping("/admin/course/note/{id}/delete")
    @ResponseBody
    public Boolean deleteAction(@PathVariable Integer id, HttpServletRequest request) {
        this.noteService.deleteNote(id, AppUser.getCurrentUser(request));
        return Boolean.TRUE;
    }

    @RequestMapping("/admin/course/note/batch_delete")
    @ResponseBody
    public Boolean batchDeleteAction(HttpServletRequest request) {
        String[] ids = request.getParameterValues("ids[]");
        if (ids != null || ids.length > 0) {
            this.noteService.deleteNotes(ids, AppUser.getCurrentUser(request));
        }
        return Boolean.TRUE;
    }
}
