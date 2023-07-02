package org.edunext.coursework.kernel.service;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.dao.QuestionCategoryDao;
import org.edunext.coursework.kernel.dao.QuestionDao;
import org.edunext.coursework.kernel.dao.QuestionFavoriteDao;
import org.edunext.coursework.kernel.service.question.type.QuestionTypeFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xulixin
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    static final String[] supportedQuestionTypes = {"choice", "single_choice", "uncertain_choice", "fill", "material", "essay", "determine"};

    private final QuestionDao questionDao;
    private final QuestionCategoryDao categoryDao;
    private final QuestionFavoriteDao questionFavoriteDao;

    public QuestionServiceImpl(QuestionDao questionDao,
                               QuestionCategoryDao categoryDao,
                               QuestionFavoriteDao questionFavoriteDao) {

        this.questionDao = questionDao;
        this.categoryDao = categoryDao;
        this.questionFavoriteDao = questionFavoriteDao;
    }


    @Override
    public Map<String, Object> getQuestion(Object id) {
        return this.questionDao.getQuestion(id);
    }

    @Override
    public Integer searchQuestionsCount(Map<String, Object> conditions) {
        return this.questionDao.searchQuestionsCount(conditions);
    }

    @Override
    public List<Map<String, Object>> searchQuestions(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        return this.questionDao.searchQuestions(conditions, orderBy, start, limit);
    }

    @Override
    public Map<String, Object> createQuestion(Map<String, Object> fields) {
        if (!ArrayUtil.inArray(fields.get("type"), supportedQuestionTypes)) {
            throw new RuntimeGoingException("question type error！");
        }

        fields = QuestionTypeFactory.create(fields.get("type")).filter(fields, "create");

        int fieldParentId = ValueParser.parseInt(fields.get("parentId"));
        if (fieldParentId > 0) {
            Map<String, Object> parentQuestion = this.getQuestion(fieldParentId);
            if (MapUtil.isEmpty(parentQuestion)) {
                fields.put("parentId", 0);
            } else {
                fields.put("target", parentQuestion.get("target"));
            }
        }

        Map<String, Object> question = this.questionDao.addQuestion(fields);

        int questionParentId = ValueParser.parseInt(question.get("parentId"));
        if (questionParentId > 0) {
            Integer subCount = this.questionDao.findQuestionsCountByParentId(questionParentId);
            this.questionDao.updateQuestion(questionParentId, FastHashMap.build(1).add("subCount", subCount).toMap());
        }

        return question;
    }

    @Override
    public List<Map<String, Object>> findCategoriesByTarget(String target, Integer start, Integer limit) {
        return this.categoryDao.findCategoriesByTarget(target, start, limit);
    }

    @Override
    public int deleteQuestion(Integer id) {
        Map<String, Object> question = this.questionDao.getQuestion(id);
        if (MapUtil.isEmpty(question)) {
            throw new RuntimeGoingException("试题不存在！不允许操作。");
        }
        int nums = this.questionDao.deleteQuestion(id);

        if (ValueParser.parseInt(question.get("subCount")) > 0) {
            nums += this.questionDao.deleteQuestionsByParentId(id);
        }

        Integer questionParentId = ValueParser.parseInt(question.get("parentId"));
        if (questionParentId > 0) {
            int subCount = this.questionDao.findQuestionsCountByParentId(questionParentId);
            this.questionDao.updateQuestion(questionParentId, FastHashMap.build(1).add("subCount", subCount).toMap());
        }
        return nums;
    }

    @Override
    public void updateQuestion(Integer id, Map<String, Object> fields) {
        Map<String, Object> question = this.getQuestion(id);
        if (MapUtil.isEmpty(question)) {
            throw new RuntimeGoingException("Question #" + id + " is not exist.");
        }

        fields = QuestionTypeFactory.create(question.get("type")).filter(fields, "update");
        if (ValueParser.parseInt(question.get("parentId")) > 0) {
            fields.remove("target");
        }

        this.questionDao.updateQuestion(id, fields);
    }

    @Override
    public List<Map<String, Object>> findQuestionsByParentId(Integer id) {
        return this.questionDao.findQuestionsByParentId(id);
    }

    @Override
    public List<Map<String, Object>> getQuestionCountGroupByTypes(Map<String, Object> conditions) {
        return this.questionDao.getQuestionCountGroupByTypes(conditions);
    }

    @Override
    public Map<String, Map<String, Object>> findQuestionsByIds(Set<Object> ids) {
        return ArrayToolkit.index(this.questionDao.findQuestionsByIds(ids), "id");
    }

    @Override
    public List<Map<String, Object>> findAllFavoriteQuestionsByUserId(Object userId) {
        return this.questionFavoriteDao.findAllFavoriteQuestionsByUserId(userId);
    }

    @Override
    public void favoriteQuestion(Integer questionId, String target, Integer userId) {
        Map<String, Object> favorite = FastHashMap.build(4)
                .add("questionId", questionId)
                .add("target", target)
                .add("userId", userId)
                .add("createdTime", System.currentTimeMillis()).toMap();

        Map<String, Object> favoriteBack = this.questionFavoriteDao.getFavoriteByQuestionIdAndTargetAndUserId(favorite);

        if (MapUtil.isEmpty(favoriteBack)) {
            this.questionFavoriteDao.addFavorite(favorite);
        }
    }

    @Override
    public void unFavoriteQuestion(Integer questionId, String target, Integer userId) {
        Map<String, Object> favorite = FastHashMap.build(3)
                .add("questionId", questionId)
                .add("target", target)
                .add("userId", userId).toMap();

        this.questionFavoriteDao.deleteFavorite(favorite);
    }

    @Override
    public Integer findFavoriteQuestionsCountByUserId(Integer userId) {
        return this.questionFavoriteDao.findFavoriteQuestionsCountByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> findFavoriteQuestionsByUserId(Integer userId, Integer start, Integer limit) {
        return this.questionFavoriteDao.findFavoriteQuestionsByUserId(userId, start, limit);
    }

    @Override
    public Map<String, Map<String, Object>> judgeQuestions(Map<String, Object[]> answers, boolean refreshStats) {
        Set<Object> questionIds = answers.keySet().stream().map(e -> (Object) e).collect(Collectors.toSet());
        List<Map<String, Object>> listForQuestions = this.questionDao.findQuestionsByIds(questionIds);
        Map<String, Map<String, Object>> questions = ArrayToolkit.index(listForQuestions, "id");

        Map<String, Map<String, Object>> results = new HashMap<>();
        for (Map.Entry<String, Object[]> entry : answers.entrySet()) {
            String id = entry.getKey();
            Object[] answer = entry.getValue();
            if (answer == null || answer.length < 1) {
                results.put(id, FastHashMap.build(1).add("status", "noAnswer").toMap());
            } else if (MapUtil.isEmpty(questions.get(id))) {
                results.put(id, FastHashMap.build(1).add("status", "notFound").toMap());
            } else {
                Map<String, Object> question = questions.get(id);
                results.put(id, QuestionTypeFactory.create(question.get("type")).judge(question, answer));
            }
        }

        return results;
    }
}
