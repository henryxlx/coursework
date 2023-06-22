package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.CourseAnnouncementDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Repository
public class CourseAnnouncementDaoImpl extends FastJdbcDaoSupport implements CourseAnnouncementDao {

    static final String TABLE_NAME = "cw_course_announcement";

    @Override
    public Map<String, Object> getAnnouncement(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
    }

    @Override
    public List<Map<String, Object>> findAnnouncementsByCourseId(Object courseId, Integer start, Integer limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE courseId=? ORDER BY createdTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, courseId);
    }

    @Override
    public List<Map<String, Object>> findAnnouncementsByCourseIds(Set<Object> ids, Integer start, Integer limit) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>(0);
        }
        String marks = repeatQuestionMark(ids.size());
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE courseId IN (" + marks + ") ORDER BY createdTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, ids.toArray());
    }

    @Override
    public Map<String, Object> addAnnouncement(Map<String, Object> fields) {
        Integer id = insertMapReturnKey(TABLE_NAME, fields).intValue();
        return this.getAnnouncement(id);
    }

    @Override
    public int deleteAnnouncement(Object id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().update(sql, id);
    }

    @Override
    public int updateAnnouncement(Object id, Map<String, Object> fields) {
        return updateMap(TABLE_NAME, fields, "id", id);
    }
}
