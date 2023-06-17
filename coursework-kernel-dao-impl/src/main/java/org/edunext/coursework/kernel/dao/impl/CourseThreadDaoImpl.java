package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.dao.CourseThreadDao;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class CourseThreadDaoImpl extends FastJdbcDaoSupport implements CourseThreadDao {

    static final String TABLE_NAME = "cw_course_thread";

    @Override
    public Map<String, Object> getThread(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
    }

    @Override
    public Map<String, Object> addThread(Map<String, Object> thread) {
        int id = insertMapReturnKey(TABLE_NAME, thread).intValue();
        return this.getThread(id);
    }

    @Override
    public Integer searchThreadCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createThreadSearchQueryBuilder(conditions).select("COUNT(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchThreads(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        DynamicQueryBuilder builder = this.createThreadSearchQueryBuilder(conditions)
                .select("*")
                .orderBy(orderBy)
                .setFirstResult(start)
                .setMaxResults(limit);

        return getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
    }

    @Override
    public void waveThread(Integer threadId, String field, Integer diff) {
        String[] fields = {"postNum", "hitNum", "followNum"};
        if (!ArrayUtil.inArray(field, field)) {
            throw new RuntimeGoingException(String.format("%s字段不允许增减，只有%s才被允许增减", field, Arrays.toString(fields)));
        }
        String sql = String.format("UPDATE %s SET %s = %s + ? WHERE id = ? LIMIT 1", TABLE_NAME, field, field);
        getJdbcTemplate().update(sql, diff, threadId);
    }

    @Override
    public Integer searchThreadCountInCourseIds(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createThreadSearchQueryBuilder(conditions).select("COUNT(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchThreadInCourseIds(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        DynamicQueryBuilder builder = this.createThreadSearchQueryBuilder(conditions)
                .select("*")
                .orderBy(orderBy)
                .setFirstResult(start)
                .setMaxResults(limit);

        return getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
    }

    @Override
    public void updateThread(Object id, Map<String, Object> fields) {
        updateMap(TABLE_NAME, fields, "id", id);
    }

    @Override
    public int deleteThread(Integer threadId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        return getJdbcTemplate().update(sql, threadId);
    }

    private DynamicQueryBuilder createThreadSearchQueryBuilder(Map<String, Object> conditions) {
        if (EasyStringUtil.isNotBlank(conditions.get("title"))) {
            conditions.put("title", "%" + conditions.get("title") + "%");
        }

        if (EasyStringUtil.isNotBlank(conditions.get("content"))) {
            conditions.put("content", "%" + conditions.get("content") + "%");
        }

        return new DynamicQueryBuilder(conditions)
                .from(TABLE_NAME)
                .andWhere("courseId = :courseId")
                .andWhere("lessonId = :lessonId")
                .andWhere("userId = :userId")
                .andWhere("type = :type")
                .andWhere("isStick = :isStick")
                .andWhere("isElite = :isElite")
                .andWhere("postNum = :postNum")
                .andWhere("postNum > :postNumLargerThan")
                .andWhere("title LIKE :title")
                .andWhere("content LIKE :content")
                .andWhere("courseId IN (:courseIds)")
                .andWhere("private = :private");
    }
}
