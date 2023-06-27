package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.*;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.FastDataDictHolder;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppUserService;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.QuestionService;
import org.edunext.coursework.kernel.service.TestPaperService;
import org.edunext.coursework.kernel.service.question.type.AbstractQuestionType;
import org.edunext.coursework.kernel.service.question.type.QuestionTypeFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseTestPaperManageController {

    private final TestPaperService testPaperService;
    private final QuestionService questionService;
    private final CourseService courseService;
    private final AppUserService userService;
    private final FastDataDictHolder dictHolder;

    public CourseTestPaperManageController(TestPaperService testPaperService,
                                           QuestionService questionService,
                                           CourseService courseService,
                                           AppUserService userService,
                                           FastDataDictHolder dictHolder) {

        this.testPaperService = testPaperService;
        this.questionService = questionService;
        this.courseService = courseService;
        this.userService = userService;
        this.dictHolder = dictHolder;
    }

    @RequestMapping("/course/{id}/manage/testpaper")
    public String indexAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);

        Map<String, Object> conditions = FastHashMap.build(1).add("target", "course-" + id).toMap();
        Paginator paginator = new Paginator(request, this.testPaperService.searchTestpapersCount(conditions), 10);

        List<Map<String, Object>> testpapers = this.testPaperService.searchTestpapers(conditions,
                OrderBy.build(1).addDesc("createdTime"),
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        model.addAttribute("users",
                this.userService.findUsersByIds(ArrayToolkit.column(testpapers, "updatedUserId")));
        model.addAttribute("paginator", paginator);
        model.addAttribute("testpapers", testpapers);
        model.addAttribute("course", course);
        return "/course/manage/testpaper/index";
    }

    @RequestMapping("/course/{courseId}/manage/testpaper/create")
    public String createAction(@PathVariable Integer courseId,
                               HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> fields = EasyWebFormEditor.toFormDataMap(request);
            AppUser.putCurrentUser(fields, request);
            fields.put("ranges", EasyStringUtil.isBlank(fields.get("ranges")) ? new String[0] :
                    String.valueOf(fields.get("ranges")).split(","));
            fields.put("target", "course-" + courseId);
            fields.put("pattern", "QuestionType");
            Integer testpaperId = this.testPaperService.createTestpaper(fields);
            return "redirect:/course/" + courseId + "/manage/testpaper/" + testpaperId + "/items";
        }

        Map<String, String> typeNames = this.dictHolder.getDict().get("questionType");
        List<Map<String, Object>> types = new ArrayList<>(typeNames.size());
        typeNames.forEach((type, name) -> {
            AbstractQuestionType typeObj = QuestionTypeFactory.create(type);
            types.add(FastHashMap.build(3)
                    .add("key", type)
                    .add("name", name)
                    .add("hasMissScore", typeObj.hasMissScore()).toMap());
        });
        model.addAttribute("types", types);

        Map<String, Object> conditions = new HashMap<>(2);
        conditions.put("types", ArrayToolkit.column(types, "key"));
        conditions.put("courseId", courseId);
        List<Map<String, Object>> questionNums = this.questionService.getQuestionCountGroupByTypes(conditions);
        model.addAttribute("questionNums", ArrayToolkit.index(questionNums, "type"));

        model.addAttribute("course", course);
        model.addAttribute("ranges", this.getQuestionRanges(course));
        return "/course/manage/testpaper/create";
    }

    private Map<String, Object> getQuestionRanges(Map<String, Object> course) {
        return getQuestionRanges(course, false);
    }

    private Map<String, Object> getQuestionRanges(Map<String, Object> course, boolean includeCourse) {
        List<Map<String, Object>> lessons = this.courseService.getCourseLessons(ValueParser.toInteger(course.get("id")));
        Map<String, Object> ranges = new HashMap<>(lessons.size());

        if (includeCourse == true) {
            ranges.put("course-" + course.get("id"), "本课程");
        }

        lessons.forEach(lesson -> {
            if ("testpaper".equals(lesson.get("type"))) {
                return;
            }
            ranges.put("course-" + lesson.get("courseId") + "/lesson-" + lesson.get("id"),
                    "课时" + lesson.get("number") + "： " + lesson.get("title"));
        });
        return ranges;
    }

    @RequestMapping("/course/{courseId}/manage/testpaper/{testpaperId}/delete")
    @ResponseBody
    public Boolean deleteAction(@PathVariable Integer courseId, @PathVariable Integer testpaperId,
                                HttpServletRequest request) {

        Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);
        Map<String, Object> testpaper = this.getTestpaperWithException(course, testpaperId);
        this.testPaperService.deleteTestpaper(testpaper.get("id"));

        return Boolean.TRUE;
    }

    @RequestMapping("/course/{courseId}/manage/testpaper/deletes")
    @ResponseBody
    public Boolean deletesAction(@PathVariable Integer courseId, HttpServletRequest request) {
        Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);

        String[] ids = request.getParameterValues("ids[]");
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                Map<String, Object> testpaper = this.getTestpaperWithException(course, id);
                this.testPaperService.deleteTestpaper(id);
            }
        }

        return Boolean.TRUE;
    }

    private Map<String, Object> getTestpaperWithException(Map<String, Object> course, Object testpaperId) {
        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testpaperId);
        if (MapUtil.isEmpty(testpaper)) {
            throw new RuntimeGoingException("试卷不存在！");
        }

        if (!("course-" + course.get("id")).equals(testpaper.get("target"))) {
            throw new RuntimeGoingException("试卷与当前课程关联错误，不能进行操作！");
        }
        return testpaper;
    }
}
