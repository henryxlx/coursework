package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.CourseNoteDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class CourseNoteDaoImpl extends FastJdbcDaoSupport implements CourseNoteDao {

    static final String TABLE_NAME = "cw_course_note";

    @Override
    public Integer getNoteCountByUserIdAndCourseId(Integer userId, Integer courseId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE userId = ? AND courseId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, userId, courseId);
    }

    @Override
    public Map<String, Object> getNoteByUserIdAndLessonId(Integer userId, Integer lessonId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE userId = ? AND lessonId = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, userId, lessonId).stream().findFirst().orElse(null);
    }

    @Override
    public void addNote(Map<String, Object> note) {
        int id = insertMapReturnKey(TABLE_NAME, note).intValue();
        note.put("id", id);
    }

    @Override
    public void updateNote(Object id, Map<String, Object> note) {
        updateMap(TABLE_NAME, note, "id", id);
    }

    @Override
    public Integer searchNoteCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchNoteQueryBuilder(conditions)
                .select("count(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchNotes(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        DynamicQueryBuilder builder = this.createSearchNoteQueryBuilder(conditions)
                .select("*")
                .orderBy(orderBy)
                .setFirstResult(start)
                .setMaxResults(limit);

        return getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
    }

    @Override
    public void deleteNote(Integer id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        getJdbcTemplate().update(sql, id);
    }

    @Override
    public Map<String, Object> getNote(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
    }

    private DynamicQueryBuilder createSearchNoteQueryBuilder(Map<String, Object> conditions) {
        if (EasyStringUtil.isNotBlank(conditions.get("content"))) {
            conditions.put("content", "%" + conditions.get("content") + "%");
        }

        return new DynamicQueryBuilder(conditions)
                .from(TABLE_NAME, "note")
                .andWhere("userId = :userId")
                .andWhere("courseId = :courseId")
                .andWhere("lessonId = :lessonId")
                .andWhere("status = :status")
                .andWhere("content LIKE :content")
                .andWhere("courseId IN (:courseIds)");
    }
}
