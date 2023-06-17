package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.CourseThreadPostDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class CourseThreadPostDaoImpl extends FastJdbcDaoSupport implements CourseThreadPostDao {

    static final String TABLE_NAME = "cw_course_thread_post";

    public Map<String, Object> getPost(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
    }

    @Override
    public Integer getPostCountByThreadId(Integer threadId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE threadId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, threadId);
    }

    @Override
    public List<Map<String, Object>> findPostsByThreadId(Integer threadId, OrderBy orderBy, Integer start, Integer limit) {
        String sql = String.format("SELECT * FROM %s WHERE threadId = ? ORDER BY %s LIMIT %d, %d",
                TABLE_NAME, orderBy.toString(), start, limit);
        return getJdbcTemplate().queryForList(sql, threadId);
    }

    @Override
    public List<Map<String, Object>> findPostsByThreadIdAndIsElite(Integer threadId, Integer isElite, Integer start, Integer limit) {
        String sql = String.format("SELECT * FROM %s WHERE threadId = ? AND isElite = ? ORDER BY createdTime ASC LIMIT %d, %d",
                TABLE_NAME, start, limit);
        return getJdbcTemplate().queryForList(sql, threadId, isElite);
    }

    @Override
    public Integer getPostCountByUserIdAndThreadId(Integer userId, Integer threadId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE userId = ? AND threadId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, userId, threadId);
    }

    @Override
    public Map<String, Object> addPost(Map<String, Object> post) {
        Integer id = insertMapReturnKey(TABLE_NAME, post).intValue();
        return getPost(id);
    }

    @Override
    public int deletePostsByThreadId(Integer threadId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE threadId = ?";
        return getJdbcTemplate().update(sql, threadId);
    }
}
