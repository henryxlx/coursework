package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface QuestionDao {

    Map<String, Object> getQuestion(Object id);

    Integer searchQuestionsCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchQuestions(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    Map<String, Object> addQuestion(Map<String, Object> fields);

    Integer findQuestionsCountByParentId(Integer parentId);

    Map<String, Object> updateQuestion(Integer id, Map<String, Object> fields);

    int deleteQuestion(Integer id);

    int deleteQuestionsByParentId(Integer parentId);
}
