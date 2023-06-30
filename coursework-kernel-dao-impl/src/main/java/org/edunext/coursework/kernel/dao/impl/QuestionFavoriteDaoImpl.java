package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.QuestionFavoriteDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class QuestionFavoriteDaoImpl extends FastJdbcDaoSupport implements QuestionFavoriteDao {

    static final String TABLE_NAME = "cw_question_favorite";

    @Override
    public List<Map<String, Object>> findAllFavoriteQuestionsByUserId(Object userId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE `userId` = ? ";
        return getJdbcTemplate().queryForList(sql, userId);
    }

    @Override
    public Map<String, Object> getFavoriteByQuestionIdAndTargetAndUserId(Map<String, Object> fields) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE questionId = :questionId AND target = :target AND userId = :userId LIMIT 1";
        return getNamedParameterJdbcTemplate().queryForList(sql, fields).stream().findFirst().orElse(new HashMap<>(0));
    }

    @Override
    public void addFavorite(Map<String, Object> fields) {
        insertMap(TABLE_NAME, fields);
    }

    @Override
    public void deleteFavorite(Map<String, Object> favorite) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE questionId = :questionId AND target = :target AND userId = :userId";
        getNamedParameterJdbcTemplate().update(sql, favorite);
    }

    @Override
    public Integer findFavoriteQuestionsCountByUserId(Integer userId) {
        String sql = "SELECT COUNT(id) FROM " + TABLE_NAME + " WHERE `userId` = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, userId);
    }

    @Override
    public List<Map<String, Object>> findFavoriteQuestionsByUserId(Integer userId, Integer start, Integer limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE `userId` = ? ORDER BY createdTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, userId);
    }

}
