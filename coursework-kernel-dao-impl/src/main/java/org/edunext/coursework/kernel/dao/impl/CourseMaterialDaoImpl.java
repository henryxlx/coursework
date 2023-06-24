package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.CourseMaterialDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class CourseMaterialDaoImpl extends FastJdbcDaoSupport implements CourseMaterialDao {

    static final String TABLE_NAME = "cw_course_material";

    @Override
    public Map<String, Object> getMaterial(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
    }

    @Override
    public Integer getMaterialCountByCourseId(Integer courseId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE courseId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, courseId);
    }

    @Override
    public List<Map<String, Object>> findMaterialsByCourseId(Integer courseId, Integer start, Integer limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE courseId=? ORDER BY createdTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, courseId);
    }

    @Override
    public Integer getMaterialCountByFileId(Object fileId) {
        String sql = "SELECT COUNT(id) FROM " + TABLE_NAME + " WHERE  fileId = ? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, fileId);
    }

    @Override
    public List<Map<String, Object>> findMaterialsByLessonId(Integer lessonId, Integer start, Integer limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE lessonId=? ORDER BY createdTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, lessonId);
    }

    @Override
    public Map<String, Object> addMaterial(Map<String, Object> fields) {
        Integer id = insertMapReturnKey(TABLE_NAME, fields).intValue();
        return getMaterial(id);
    }

    @Override
    public void deleteMaterial(Object id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        getJdbcTemplate().update(sql, id);
    }

    @Override
    public Integer getLessonMaterialCount(Integer courseId, Integer lessonId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE  courseId = ? AND lessonId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, courseId, lessonId);
    }
}
