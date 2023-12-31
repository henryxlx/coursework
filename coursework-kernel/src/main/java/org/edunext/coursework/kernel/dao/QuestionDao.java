package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    List<Map<String, Object>> findQuestionsByParentId(Integer id);

    List<Map<String, Object>> getQuestionCountGroupByTypes(Map<String, Object> conditions);

    List<Map<String, Object>> findQuestionsByIds(Set<Object> ids);

    void updateQuestionCountByIds(Set<String> ids, String status);
}
