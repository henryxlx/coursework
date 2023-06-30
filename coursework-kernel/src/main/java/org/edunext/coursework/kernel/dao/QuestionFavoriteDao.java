package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface QuestionFavoriteDao {

    List<Map<String, Object>> findAllFavoriteQuestionsByUserId(Object userId);

    Map<String, Object> getFavoriteByQuestionIdAndTargetAndUserId(Map<String, Object> fields);

    void addFavorite(Map<String, Object> fields);

    void deleteFavorite(Map<String, Object> favorite);

    Integer findFavoriteQuestionsCountByUserId(Integer userId);

    List<Map<String, Object>> findFavoriteQuestionsByUserId(Integer userId, Integer start, Integer limit);
}
