package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.FavoriteDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class FavoriteDaoImpl extends FastJdbcDaoSupport implements FavoriteDao {

    static final String TABLE_NAME = "cw_course_favorite";

    public Map<String, Object> getFavorite(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
    }

    @Override
    public Integer getFavoriteCourseCountByUserId(Integer userId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE  userId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, userId);
    }

    @Override
    public List<Map<String, Object>> findCourseFavoritesByUserId(Integer userId, Integer start, Integer limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE userId = ? ORDER BY createdTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, userId);
    }

    @Override
    public Map<String, Object> getFavoriteByUserIdAndCourseId(Integer userId, Integer courseId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE userId = ? AND courseId = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, userId, courseId).stream().findFirst().orElse(null);
    }

    @Override
    public void addFavorite(Map<String, Object> fields) {
        Integer id = insertMapReturnKey(TABLE_NAME, fields).intValue();
        fields.put("Id", id);
    }

    @Override
    public void deleteFavorite(Object id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        getJdbcTemplate().update(sql, id);
    }
}
