package org.edunext.coursework.kernel.service;

import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.dao.QuestionCategoryDao;
import org.edunext.coursework.kernel.dao.QuestionDao;
import org.edunext.coursework.kernel.service.question.type.QuestionTypeFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    static final String[] supportedQuestionTypes = {"choice", "single_choice", "uncertain_choice", "fill", "material", "essay", "determine"};

    private final QuestionDao questionDao;
    private final QuestionCategoryDao categoryDao;

    public QuestionServiceImpl(QuestionDao questionDao, QuestionCategoryDao categoryDao) {
        this.questionDao = questionDao;
        this.categoryDao = categoryDao;
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
}
