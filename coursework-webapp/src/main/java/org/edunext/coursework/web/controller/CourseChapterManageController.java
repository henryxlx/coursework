package org.edunext.coursework.web.controller;

import com.jetwinner.util.ArrayUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseChapterManageController {

    private final CourseService courseService;
    private final AppSettingService settingService;

    public CourseChapterManageController(CourseService courseService,
                                         AppSettingService settingService) {

        this.courseService = courseService;
        this.settingService = settingService;
    }

    @RequestMapping("/course/{id}/manage/chapter/create")
    public String createAction(HttpServletRequest request, @PathVariable Integer id, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);

        String type = request.getParameter("type");
        type = ArrayUtil.inArray(type, "chapter", "unit") ? type : "chapter";
        String parentId = request.getParameter("parentId");

        model.addAttribute("course", course);
        model.addAttribute("default", settingService.get("default"));

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> chapter = ParamMap.toFormDataMap(request);
            chapter.put("courseId", course.get("id"));
            chapter = courseService.createChapter(chapter);
            model.addAttribute("chapter", chapter);
            model.addAttribute("chapter_seq", chapter.get("seq"));

            return "/course/manage/chapter/list-item";
        }

        model.addAttribute("type", type);
        model.addAttribute("parentId", parentId);

        return "/course/manage/chapter/chapter-modal";
    }
}
