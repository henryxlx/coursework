package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.QuestionDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class QuestionDaoImpl implements QuestionDao {

    @Override
    public Map<String, Object> getQuestion(Object id) {
        return null;
    }

    @Override
    public Integer searchQuestionsCount(Map<String, Object> conditions) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> searchQuestions(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        return null;
    }

    @Override
    public Map<String, Object> addQuestion(Map<String, Object> fields) {
        return null;
    }

    @Override
    public Integer findQuestionsCountByParentId(Integer parentId) {
        return 0;
    }

    @Override
    public void updateQuestion(Integer parentId, Map<String, Object> subCount) {

    }
}
