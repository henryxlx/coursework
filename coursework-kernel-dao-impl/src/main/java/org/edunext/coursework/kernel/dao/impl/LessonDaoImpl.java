package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.dao.LessonDao;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Repository
public class LessonDaoImpl extends FastJdbcDaoSupport implements LessonDao {

    private static final String TABLE_NAME = "cw_course_lesson";

    @Override
    public Map<String, Object> getLesson(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(MapUtil.newHashMap(0));
    }

    @Override
    public List<Map<String, Object>> findLessonsByCourseId(Object courseId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE courseId = ? ORDER BY seq ASC";
        return getJdbcTemplate().queryForList(sql, courseId);
    }

    @Override
    public int getLessonMaxSeqByCourseId(Object courseId) {
        String sql = "SELECT MAX(seq) FROM " + TABLE_NAME + " WHERE courseId = ?";
        return getJdbcTemplate().query(sql, (rs, rowNum) -> new Integer(ValueParser.parseInt(rs.getObject(1))), courseId)
                .stream().findFirst().orElse(0);
    }

    @Override
    public List<Map<String, Object>> findLessonsByChapterId(Object chapterId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE chapterId = ? ORDER BY seq ASC";
        return getJdbcTemplate().queryForList(sql, chapterId);
    }

    @Override
    public void batchUpdateLesson(List<Map<String, Object>> lessons, String pKeyName, String... updateFieldNames) {
        StringBuilder buf = new StringBuilder().append("UPDATE ").append(TABLE_NAME).append(" SET ");
        if (updateFieldNames != null && updateFieldNames.length > 0) {
            for (int i = 0, len = updateFieldNames.length; i < len; i++) {
                if (i > 0) {
                    buf.append(" AND ");
                }
                buf.append(" ").append(updateFieldNames[i]).append(" = :").append(updateFieldNames[i]).append(" ");
            }
        }
        buf.append(" WHERE ").append(pKeyName).append(" = :").append(pKeyName);
        getNamedParameterJdbcTemplate().batchUpdate(buf.toString(),
                SqlParameterSourceUtils.createBatch(lessons.toArray()));
    }

    @Override
    public Map<String, Object> addLesson(Map<String, Object> lesson) {
        int id = insertMapReturnKey(TABLE_NAME, lesson).intValue();
        if (id <= 0) {
            throw new RuntimeGoingException("Insert course lesson error.");
        }
        return this.getLesson(id);
    }

    @Override
    public Integer getLessonCountByCourseId(Object courseId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE courseId = ? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, courseId);
    }

    @Override
    public Integer sumLessonGiveCreditByCourseId(Object courseId) {
        String sql = "SELECT SUM(giveCredit) FROM " + TABLE_NAME + " WHERE  courseId = ?";
        return getJdbcTemplate().queryForList(sql, Integer.class, courseId)
                .stream().findFirst().orElse(0);
    }

    @Override
    public void updateLesson(Integer id, Map<String, Object> fields) {
        updateMap(TABLE_NAME, fields, "id", id);
    }

    @Override
    public List<Map<String, Object>> findLessonsByIds(Set<Object> ids) {
        if (ids == null || ids.size() < 1) {
            return new ArrayList<>(0);
        }
        String marks = repeatQuestionMark(ids.size());
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id IN (" + marks + ");";
        return getJdbcTemplate().queryForList(sql, ids.toArray());
    }

    @Override
    public Integer searchLessonCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions).select("COUNT(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchLessons(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        DynamicQueryBuilder builder = createSearchQueryBuilder(conditions)
                .select("*")
                .orderBy(orderBy)
                .setFirstResult(start)
                .setMaxResults(limit);
        return getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
    }

    private DynamicQueryBuilder createSearchQueryBuilder(Map<String, Object> conditions) {
        return new DynamicQueryBuilder(conditions)
                .from(TABLE_NAME)
                .andWhere("courseId = :courseId")
                .andWhere("status = :status")
                .andWhere("type = :type")
                .andWhere("free = :free")
                .andWhere("userId = :userId")
                .andWhere("mediaId = :mediaId")
                .andWhere("startTime >= :startTimeGreaterThan")
                .andWhere("endTime < :endTimeLessThan")
                .andWhere("startTime <= :startTimeLessThan")
                .andWhere("endTime > :endTimeGreaterThan")
                .andWhere("title LIKE :titleLike")
                .andWhere("createdTime >= :startTime")
                .andWhere("createdTime <= :endTime")
                .andWhere("courseId IN ( :courseIds )");
    }
}
