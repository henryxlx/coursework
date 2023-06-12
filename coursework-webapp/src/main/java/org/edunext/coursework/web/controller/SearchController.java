package org.edunext.coursework.web.controller;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller("courseworkSearchController")
public class SearchController {

    private final AppCategoryService categoryService;
    private final CourseService courseService;

    public SearchController(AppCategoryService categoryService, CourseService courseService) {
        this.categoryService = categoryService;
        this.courseService = courseService;
    }

    @RequestMapping("/search")
    public String indexPage(HttpServletRequest request, Model model) {
        String keywords = request.getParameter("q");
        keywords = EasyStringUtil.trim(keywords);

        int parentId = 0;
        List<Map<String, Object>> categories = this.categoryService.findAllCategoriesByParentId(parentId);
        Map<String, Object> categoryIds = MapUtil.newHashMap(categories.size());
        categories.forEach(e -> categoryIds.put(String.valueOf(e.get("id")), e.get("name")));

        String categoryId = request.getParameter("categoryIds");
        String coursesTypeChoices = request.getParameter("coursesTypeChoices");

        if (EasyStringUtil.isBlank(keywords)) {
            model.addAttribute("categoryIds", categoryIds);
            model.addAttribute("coursesTypeChoices", coursesTypeChoices);
            return "/search/index";
        }

        Map<String, Object> conditions = new ParamMap()
                .add("status", "published").add("title", keywords).toMap();
        if (EasyStringUtil.isNotBlank(categoryId)) {
            conditions.put("categoryId", categoryId);
        }
        if ("liveCourses".equals(coursesTypeChoices)) {
            conditions.put("type", "live");
        } else if ("freeCourses".equals(coursesTypeChoices)) {
            conditions.put("price", "0.00");
        }

        Paginator paginator = new Paginator(request, this.courseService.searchCourseCount(conditions), 10);
        model.addAttribute("courses", this.courseService.searchCourses(conditions,
                "latest",
                paginator.getOffsetCount(),
                paginator.getPerPageCount()));

        model.addAttribute("paginator", paginator);
        model.addAttribute("keywords", keywords);
        model.addAttribute("categoryIds", categoryIds);
        model.addAttribute("coursesTypeChoices", coursesTypeChoices);
        return "/search/index";
    }
}
