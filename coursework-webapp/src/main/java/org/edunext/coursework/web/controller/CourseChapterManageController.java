package org.edunext.coursework.web.controller;

import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("/course/{courseId}/manage/chapter/{chapterId}/edit")
    public ModelAndView editAction(@PathVariable Integer courseId, @PathVariable Integer chapterId,
                                   HttpServletRequest request) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);
        Map<String, Object> chapter = courseService.getChapter(courseId, chapterId);
        if (MapUtil.isEmpty(chapter)) {
            throw new RuntimeGoingException("章节(#" + chapterId + ")不存在！");
        }

        ModelAndView mav = new ModelAndView();
        if ("POST".equals(request.getMethod())) {
            Map<String, Object> fields = ParamMap.toCustomFormDataMap(request);
            fields.put("courseId", course.get("id"));
            chapter = courseService.updateChapter(courseId, chapterId, fields);
            mav.setViewName("/course/manage/chapter/list-item");
            mav.addObject("course", course);
            mav.addObject("chapter", chapter);
            return mav;
        }

        mav.setViewName("/course/manage/chapter/chapter-modal");
        mav.addObject("course", course);
        mav.addObject("chapter", chapter);
        mav.addObject("type", chapter.get("type"));
        mav.addObject("default", settingService.get("default"));
        return mav;
    }

    @RequestMapping("/course/{courseId}/manage/chapter/{chapterId}/delete")
    @ResponseBody
    public Boolean deleteAction(@PathVariable Integer courseId, @PathVariable Integer chapterId,
                                HttpServletRequest request) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);
        if (MapUtil.isNotEmpty(course)) {
            courseService.deleteChapter(courseId, chapterId);
        }
        return Boolean.TRUE;
    }
}
