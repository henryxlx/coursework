package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
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
}
