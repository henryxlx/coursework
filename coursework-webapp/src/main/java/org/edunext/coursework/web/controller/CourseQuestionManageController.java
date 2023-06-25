package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.*;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.QuestionService;
import org.edunext.coursework.kernel.service.question.finder.TargetHelperBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseQuestionManageController {

    private final QuestionService questionService;
    private final CourseService courseService;
    private final TargetHelperBean targetHelperBean;
    private final AppUserService userService;
    private final AppSettingService settingService;

    public CourseQuestionManageController(QuestionService questionService,
                                          CourseService courseService,
                                          TargetHelperBean targetHelperBean,
                                          AppUserService userService,
                                          AppSettingService settingService) {

        this.questionService = questionService;
        this.courseService = courseService;
        this.targetHelperBean = targetHelperBean;
        this.userService = userService;
        this.settingService = settingService;
    }

    @RequestMapping("/course/{courseId}/manage/question")
    public String questionAction(@PathVariable Integer courseId, HttpServletRequest request, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);

        Map<String, Object> conditions = ParamMap.toQueryAllMap(request);

        if (EasyStringUtil.isBlank(conditions.get("target"))) {
            conditions.put("targetPrefix", "course-" + courseId);
        }

        if (EasyStringUtil.isNotBlank(conditions.get("keyword"))) {
            conditions.put("stem", conditions.get("keyword"));
        }

        Map<String, Object> parentQuestion;
        OrderBy orderBy;
        if (ValueParser.parseInt(conditions.get("parentId")) > 0) {
            parentQuestion = this.questionService.getQuestion(conditions.get("parentId"));
            if (MapUtil.isEmpty(parentQuestion)) {
                return "/course/" + courseId + "/manage/question";
            }

            orderBy = OrderBy.build(1).addAsc("createdTime");
        } else {
            conditions.put("parentId", 0);
            parentQuestion = null;
            orderBy = OrderBy.build(1).addDesc("createdTime");
        }

        Paginator paginator = new Paginator(request, this.questionService.searchQuestionsCount(conditions), 10);

        List<Map<String, Object>> questions = this.questionService.searchQuestions(conditions, orderBy,
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        model.addAttribute("users", this.userService.findUsersByIds(ArrayToolkit.column(questions, "userId")));

        model.addAttribute("targets", this.targetHelperBean.getTargets(ArrayToolkit.column(questions, "target")));

        model.addAttribute("targetChoices", this.getQuestionTargetChoices(course));
        model.addAttribute("parentQuestion", parentQuestion);
        model.addAttribute("course", course);
        model.addAttribute("questions", questions);
        model.addAttribute("paginator", paginator);
        model.addAttribute("conditions", conditions);
        return "/course/manage/question/index";
    }

    @RequestMapping("/course/{courseId}/manage/question/create/{type}")
    public String createAction(@PathVariable Integer courseId, @PathVariable String type,
                               HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> data = EasyWebFormEditor.toFormDataMap(request);
            AppUser.putCurrentUser(data, request);
            Map<String, Object> question = this.questionService.createQuestion(data);

            if ("continue".equals(data.get("submission"))) {
                Map<String, Object> urlParams = ArrayToolkit.part(question, "target", "difficulty", "parentId");
                urlParams.put("type", type);
                urlParams.put("courseId", courseId);
                urlParams.put("goto", request.getParameter("goto"));
                BaseControllerHelper.setFlashMessage("success", "题目添加成功，请继续添加。", request.getSession());
                return "redirect:/course/" + courseId + "/manage/question/create/" + type;
            } else if ("continue_sub".equals(data.get("submission"))) {
                BaseControllerHelper.setFlashMessage("success", "题目添加成功，请继续添加子题。", request.getSession());
                String gotoUrl = request.getParameter("goto");
                gotoUrl = EasyStringUtil.isBlank(gotoUrl) ?
                        "/course/" + courseId + "/manage/question?parentId=" + question.get("id") :
                        gotoUrl;
                return "redirect:" + gotoUrl;
            } else {
                BaseControllerHelper.setFlashMessage("success", "题目添加成功。", request.getSession());
                String gotoUrl = request.getParameter("goto");
                gotoUrl = EasyStringUtil.isBlank(gotoUrl) ? "/course/" + courseId + "/manage/question" : gotoUrl;
                return "redirect:" + gotoUrl;
            }
        }

        Map<String, Object> question = FastHashMap.build(5)
                .add("id", 0)
                .add("type", type)
                .add("target", request.getParameter("target"))
                .add("difficulty", ServletRequestUtils.getStringParameter(request, "difficulty", "normal"))
                .add("parentId", ServletRequestUtils.getIntParameter(request, "parentId", 0)).toMap();

        Map<String, Object> parentQuestion;
        if (ValueParser.parseInt(question.get("parentId")) > 0) {
            parentQuestion = this.questionService.getQuestion(question.get("parentId"));
            if (MapUtil.isEmpty(parentQuestion)) {
                ModelAndView mav = BaseControllerHelper.createMessageResponse("error", "父题不存在，不能创建子题！");
                model.addAllAttributes(mav.getModel());
                return mav.getViewName();
            }
        } else {
            parentQuestion = null;
        }
        model.addAttribute("parentQuestion", parentQuestion);

        Map<String, Object> features = this.settingService.get("enabled_features");
        model.addAttribute("enabledAudioQuestion", features.containsKey("audio_question"));

        model.addAttribute("course", course);
        model.addAttribute("question", question);
        model.addAttribute("targetsChoices", this.getQuestionTargetChoices(course));
        model.addAttribute("categoryChoices", this.getQuestionCategoryChoices(course));
        return "/course/manage/question/question-form-" + type;
    }

    private Map<String, Object> getQuestionCategoryChoices(Map<String, Object> course) {
        List<Map<String, Object>> categories = this.questionService.findCategoriesByTarget("course-" + course.get("id"), 0, QuestionService.MAX_CATEGORY_QUERY_COUNT);
        if (ListUtil.isEmpty(categories)) {
            return null;
        }
        Map<String, Object> choices = new HashMap<>(categories.size());
        categories.forEach(category -> choices.put(String.valueOf(category.get("id")), category.get("name")));
        return choices;
    }

    private Map<String, Object> getQuestionTargetChoices(Map<String, Object> course) {
        List<Map<String, Object>> lessons = this.courseService.getCourseLessons(ValueParser.toInteger(course.get("id")));
        Map<String, Object> choices = new HashMap<>();
        choices.put("course-" + course.get("id"), "本课程");
        lessons.forEach(lesson -> {
            if ("testpaper".equals(lesson.get("type"))) {
                return;
            }
            choices.put("course-" + course.get("id") + "/lesson-" + lesson.get("id"),
                    "课时" + lesson.get("number") + "：" + lesson.get("title"));
        });
        return choices;
    }

    @RequestMapping("/course/{courseId}/manage/question/delete/{id}")
    @ResponseBody
    public Boolean deleteAction(@PathVariable Integer courseId, @PathVariable Integer id,
                                HttpServletRequest request) {

        Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);
        Map<String, Object> question = this.questionService.getQuestion(id);
        this.questionService.deleteQuestion(id);

        return Boolean.TRUE;
    }

    @RequestMapping("/course/{courseId}/manage/question/deletes")
    @ResponseBody
    public Boolean deletesAction(@PathVariable Integer courseId, HttpServletRequest request) {
        Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), courseId);

        String[] ids = request.getParameterValues("ids[]");
        for (int i = 0, len = ids == null ? 0 : ids.length; i < len; i++) {
            Integer id = ValueParser.toInteger(ids[i]);
            this.questionService.deleteQuestion(id);
        }
        return Boolean.TRUE;
    }
}