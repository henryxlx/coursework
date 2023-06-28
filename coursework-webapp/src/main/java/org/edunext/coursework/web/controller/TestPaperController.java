package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.TestPaperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class TestPaperController {

    private final CourseService courseService;
    private final TestPaperService testPaperService;

    public TestPaperController(CourseService courseService, TestPaperService testPaperService) {
        this.courseService = courseService;
        this.testPaperService = testPaperService;
    }

    @RequestMapping("/course/{id}/check/{status}/list")
    public String teacherCheckInCourseAction(@PathVariable Integer id, @PathVariable String status,
                                             HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course", course);
        return "/my/quiz/list-course-test-paper";
    }

    @RequestMapping("/test/{testId}/preview")
    public String previewTestAction(@PathVariable Integer testId, Model model) {
        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testId);

//        if (!this.testPaperService.canTeacherCheck(testpaper.get("id"))) {
//            throw new RuntimeGoingException("无权预览试卷！");
//        }

        Map<String, Map<String, Object>> items = this.testPaperService.previewTestpaper(testId);

//        model.addAttribute("total", this.makeTestpaperTotal(testpaper, items));

        model.addAttribute("items", items);
        model.addAttribute("limitTime", ValueParser.parseInt(testpaper.get("limitedTime")) * 60);
        model.addAttribute("paper", testpaper);
        model.addAttribute("id", 0);
        model.addAttribute("isPreview", "preview");
        return "quiz/test/testpaper-show";
    }

    private Map<String, Object> makeTestpaperTotal(Map<String, Object> testpaper,
                                                   Map<String, Map<String, Object>> items) {

        Map testPaperMetas = ArrayToolkit.toMap(testpaper.get("metas"));
        Map<String, Object> total = new HashMap<>(items.keySet().size());
        List<Object> list = (List<Object>) testPaperMetas.get("question_type_seq");
        for (Object obj : list) {
            String type = String.valueOf(obj);
            if (items.get(type) == null) {
                total.put(type, FastHashMap.build(3).add("score", 0).add("number", 0).add("missScore", 0).toMap());
            } else {
                Map<String, Object> map = new HashMap<>(3);
//                map.put("score", array_sum(ArrayToolkit.column(items.get(type), "score"));
                map.put("number", items.get(type).values().size());
                map.put("missScore", 0);
                if (testPaperMetas.containsKey("missScore")) {
                    Map<String, Object> mapForMiss = ArrayToolkit.toMap(testPaperMetas.get("missScore"));
                    if (mapForMiss.containsKey(type)) {
                        map.put("missScore", mapForMiss.get(type));
                    }
                }
                total.put(type, map);
            }
        }
        return total;
    }
}
