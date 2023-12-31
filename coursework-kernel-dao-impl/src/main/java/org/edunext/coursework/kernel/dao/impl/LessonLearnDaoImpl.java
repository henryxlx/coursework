package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.LessonLearnDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class LessonLearnDaoImpl extends FastJdbcDaoSupport implements LessonLearnDao {

    static final String TABLE_NAME = "cw_course_lesson_learn";

    @Override
    public Integer findLearnsCountByLessonId(Integer lessonId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE lessonId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, lessonId);
    }

    @Override
    public List<Map<String, Object>> findLearnsByLessonId(Integer lessonId, Integer start, Integer limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE lessonId = ? ORDER BY startTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, lessonId);
    }

    @Override
    public Integer getLearnCountByUserIdAndCourseIdAndStatus(Object userId, Object courseId, String status) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE userId = ? AND courseId = ? AND status = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, userId, courseId, status);
    }

    @Override
    public void deleteLearnsByLessonId(Integer lessonId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE lessonId = ?";
        getJdbcTemplate().update(sql, lessonId);
    }

    @Override
    public Map<String, Object> getLearnByUserIdAndLessonId(Integer userId, Integer lessonId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE userId=? AND lessonId=?";
        return getJdbcTemplate().queryForList(sql, userId, lessonId).stream().findFirst().orElse(null);
    }

    @Override
    public void addLearn(Map<String, Object> fields) {
        insertMap(TABLE_NAME, fields);
    }

    @Override
    public void updateLearn(Object id, Map<String, Object> fields) {
        updateMap(TABLE_NAME, fields, "id", id);
    }

    @Override
    public List<Map<String, Object>> findLearnsByUserIdAndCourseIdAndStatus(Object userId, Integer courseId, String status) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE userId=? AND courseId=? AND status = ?";
        return getJdbcTemplate().queryForList(sql, userId, courseId, status);
    }

    @Override
    public Integer searchLearnTime(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions)
                .select("sum(learnTime)");

        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions,
                (rs, rowNum) -> new Integer(ValueParser.parseInt(rs.getObject(1))));
    }

    @Override
    public List<Map<String, Object>> findLearnsByUserIdAndCourseId(Integer userId, Object courseId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE userId=? AND courseId=?";
        return getJdbcTemplate().queryForList(sql, userId, courseId);
    }

    @Override
    public Integer searchLearnCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions).select("count(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public Integer searchWatchTime(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions).select("sum(watchTime)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchLearns(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions)
                .select("*")
                .orderBy(orderBy)
                .setFirstResult(start)
                .setMaxResults(limit);
        return getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
    }

    private DynamicQueryBuilder createSearchQueryBuilder(Map<String, Object> conditions) {
        DynamicQueryBuilder builder;
        if (EasyStringUtil.isNotBlank(conditions.get("targetType"))) {
            builder = new DynamicQueryBuilder(conditions)
                    .from(TABLE_NAME)
                    .andWhere("status = :status")
                    .andWhere("finishedTime >= :startTime")
                    .andWhere("finishedTime <= :endTime");
        } else {
            builder = new DynamicQueryBuilder(conditions)
                    .from(TABLE_NAME)
                    .andWhere("status = :status")
                    .andWhere("userId = :userId")
                    .andWhere("lessonId = :lessonId")
                    .andWhere("courseId = :courseId")
                    .andWhere("finishedTime >= :startTime")
                    .andWhere("finishedTime <= :endTime");
        }

        builder.andWhere("courseId IN (:courseIds)");

        return builder;
    }
}
