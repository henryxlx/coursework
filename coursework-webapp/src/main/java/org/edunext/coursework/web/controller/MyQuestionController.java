package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.service.QuestionService;
import org.edunext.coursework.kernel.service.TestPaperService;
import org.edunext.coursework.kernel.service.question.finder.TargetHelperBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author xulixin
 */
@Controller
public class MyQuestionController {

    private final QuestionService questionService;
    private final TestPaperService testPaperService;
    private final TargetHelperBean targetHelperBean;

    public MyQuestionController(QuestionService questionService,
                                TestPaperService testPaperService,
                                TargetHelperBean targetHelperBean) {

        this.questionService = questionService;
        this.testPaperService = testPaperService;
        this.targetHelperBean = targetHelperBean;
    }

    @PostMapping("/question/{id}/favorite")
    @ResponseBody
    public Boolean favoriteQuestionAction(@PathVariable Integer id, HttpServletRequest request) {
        String targetType = request.getParameter("targetType");
        String targetId = request.getParameter("targetId");
        String target = targetType + "-" + targetId;

        AppUser user = AppUser.getCurrentUser(request);

        this.questionService.favoriteQuestion(id, target, user.getId());

        return Boolean.TRUE;
    }

    @PostMapping("/question/{id}/unfavorite")
    @ResponseBody
    public Boolean unFavoriteQuestionAction(@PathVariable Integer id, HttpServletRequest request) {
        String targetType = request.getParameter("targetType");
        String targetId = request.getParameter("targetId");
        String target = targetType + "-" + targetId;

        AppUser user = AppUser.getCurrentUser(request);

        this.questionService.unFavoriteQuestion(id, target, user.getId());

        return Boolean.TRUE;
    }

    @RequestMapping("/my/favorite/question/show")
    public String showFavoriteQuestionAction(HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);

        Paginator paginator = new Paginator(request,
                this.questionService.findFavoriteQuestionsCountByUserId(user.getId()), 10);

        List<Map<String, Object>> favoriteQuestions = this.questionService.findFavoriteQuestionsByUserId(user.getId(),
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        Set<Object> questionIds = ArrayToolkit.column(favoriteQuestions, "questionId");

        Map<String, Map<String, Object>> questions = this.questionService.findQuestionsByIds(questionIds);

        Set<Object> myTestpaperIds = new HashSet<>();

        Map<String, Map<String, Object>> targets = targetHelperBean.getTargets(ArrayToolkit.column(favoriteQuestions, "target"));

        favoriteQuestions.forEach(v -> {
            Map<String, Object> mapTarget = targets.get(v.get("target"));
            if (mapTarget != null && "testpaper".equals(mapTarget.get("type"))) {
                myTestpaperIds.add(mapTarget.get("id"));
            }
        });

        List<Map<String, Object>> myTestpapers = this.testPaperService.findTestpapersByIds(myTestpaperIds);

        model.addAttribute("favoriteActive", "active");
        model.addAttribute("user", user);
        model.addAttribute("favoriteQuestions", ArrayToolkit.index(favoriteQuestions, "id"));
        model.addAttribute("testpapers", ArrayToolkit.index(myTestpapers, "id"));
        model.addAttribute("questions", questions);
        model.addAttribute("targets", targets);
        model.addAttribute("paginator", paginator);
        return "/my/quiz/my-favorite-question";
    }

    @RequestMapping("/my/favorite/question/{id}/preview")
    public String previewAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> question = this.questionService.getQuestion(id);
        if (MapUtil.isEmpty(question)) {
            throw new RuntimeGoingException("题目不存在！");
        }
        List<Map<String, Object>> myFavorites =
                this.questionService.findAllFavoriteQuestionsByUserId(AppUser.getCurrentUser(request).getId());

        Set<Object> questionIds = ArrayToolkit.column(myFavorites, "questionId");
        if (!ArrayUtil.inArray("" + question.get("id"), questionIds.stream().map(String::valueOf).toArray(String[]::new))) {
            throw new RuntimeGoingException("无权预览非本人收藏的题目!");
        }

        Map<String, Object> item = FastHashMap.build(4)
                .add("questionId", question.get("id"))
                .add("questionType", question.get("type"))
                .add("question", question).toMap();

        if ("material".equals(question.get("type"))) {
            List<Map<String, Object>> questions = this.questionService.findQuestionsByParentId(id);

            List<Map<String, Object>> items = new ArrayList<>(questions.size());
            questions.forEach(value -> FastHashMap.build(3)
                    .add("questionId", value.get("id"))
                    .add("questionType", value.get("type"))
                    .add("question", value).toMap());

            item.put("items", items);
        }
        model.addAttribute("item", item);

        model.addAttribute("type",
                ArrayUtil.inArray(question.get("type"), "single_choice", "uncertain_choice") ? "choice" : question.get("type"));
        model.addAttribute("questionPreview", Boolean.TRUE);

        return "/quiz/test/question-preview-modal";
    }
}
