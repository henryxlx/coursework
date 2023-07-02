package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    int deleteQuestion(Integer id);

    void updateQuestion(Integer id, Map<String, Object> fields);

    List<Map<String, Object>> findQuestionsByParentId(Integer id);

    List<Map<String, Object>> getQuestionCountGroupByTypes(Map<String, Object> conditions);

    Map<String, Map<String, Object>> findQuestionsByIds(Set<Object> ids);

    List<Map<String, Object>> findAllFavoriteQuestionsByUserId(Object userId);

    void favoriteQuestion(Integer questionId, String target, Integer userId);

    void unFavoriteQuestion(Integer questionId, String target, Integer userId);

    Integer findFavoriteQuestionsCountByUserId(Integer userId);

    List<Map<String, Object>> findFavoriteQuestionsByUserId(Integer userId, Integer start, Integer limit);

    Map<String, Map<String, Object>> judgeQuestions(Map<String, Object[]> answers, boolean refreshStats);

    void statQuestionTimes(Map<String, Map<String, Object>> answers);
}
