package org.edunext.coursework.web.controller;

import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppTagService;
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
@Controller("frontCourseTagController")
public class TagController {

    private final AppTagService tagService;
    private final CourseService courseService;

    public TagController(AppTagService tagService, CourseService courseService) {
        this.tagService = tagService;
        this.courseService = courseService;
    }

    @RequestMapping("/tag/{id}")
    public String showAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> tag = this.tagService.getTag(id);

        if (MapUtil.isNotEmpty(tag)) {
            Map<String, Object> conditions = new ParamMap()
                    .add("status", "published").add("tagId", tag.get("id")).toMap();

            Paginator paginator = new Paginator(request, this.courseService.searchCourseCount(conditions), 10);

            model.addAttribute("courses", this.courseService.searchCourses(conditions, "latest",
                    paginator.getOffsetCount(),
                    paginator.getPerPageCount()));

            model.addAttribute("paginator", paginator);
            model.addAttribute("tag", tag);
        }

        return "/tag/show";
    }
}
