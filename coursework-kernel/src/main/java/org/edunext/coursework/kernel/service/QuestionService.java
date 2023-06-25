package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface QuestionService {

    Integer MAX_CATEGORY_QUERY_COUNT = 1000;

    Map<String, Object> getQuestion(Object id);

    Integer searchQuestionsCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchQuestions(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    Map<String, Object> createQuestion(Map<String, Object> fields);

    List<Map<String, Object>> findCategoriesByTarget(String target, Integer start, Integer limit);
}
