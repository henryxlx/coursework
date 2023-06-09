package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.ReviewDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class ReviewDaoImpl extends FastJdbcDaoSupport implements ReviewDao {

    static final String TABLE_NAME = "cw_course_review";

    @Override
    public Integer getReviewCountByCourseId(Integer courseId) {
        String sql = "SELECT COUNT(id) FROM " + TABLE_NAME + " WHERE courseId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, courseId);
    }

    @Override
    public List<Map<String, Object>> findReviewsByCourseId(Integer courseId, Integer start, Integer limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE courseId = ? ORDER BY createdTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, courseId);
    }

    @Override
    public Map<String, Object> getReviewByUserIdAndCourseId(Integer userId, Integer courseId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE courseId = ? AND userId = ? LIMIT 1;";
        return getJdbcTemplate().queryForList(sql, courseId, userId).stream().findFirst().orElse(null);
    }

    @Override
    public int addReview(Map<String, Object> fields) {
        return insertMap(TABLE_NAME, fields);
    }

    @Override
    public int updateReview(Object id, Map<String, Object> fields) {
        return updateMap(TABLE_NAME, fields, "id", id);
    }

    @Override
    public int getReviewRatingSumByCourseId(Integer courseId) {
        String sql = "SELECT sum(rating) FROM " + TABLE_NAME + " WHERE courseId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, courseId);
    }
}
