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
    public Map<String, Object> findPostsByThreadIdAndIsElite(Integer threadId, Integer isElite, Integer start, Integer limit) {
        String sql = String.format("SELECT * FROM %s WHERE threadId = ? AND isElite = ? ORDER BY createdTime ASC LIMIT %d, %d",
                TABLE_NAME, start, limit);
        return getJdbcTemplate().queryForList(sql, threadId, isElite).stream().findFirst().orElse(null);
    }
}
