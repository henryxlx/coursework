package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.CourseChapterDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class CourseChapterDaoImpl extends FastJdbcDaoSupport implements CourseChapterDao {

    private static final String TABLE_NAME = "cw_course_chapter";

    @Override
    public List<Map<String, Object>> findChaptersByCourseId(Object courseId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE courseId = ? ORDER BY createdTime ASC";
        return getJdbcTemplate().queryForList(sql, courseId);
    }

    @Override
    public Map<String, Object> getChapter(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(new HashMap<>(0));
    }

    @Override
    public int deleteChapter(Object chapterId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        return getJdbcTemplate().update(sql, chapterId);
    }

    @Override
    public Map<String, Object> updateChapter(Integer chapterId, Map<String, Object> fields) {
        fields.put("id", chapterId);
        int nums = updateMap(TABLE_NAME, fields, "id");
        return nums > 0 ? getChapter(chapterId) : MapUtil.newHashMap(0);
    }

    @Override
    public Map<String, Object> addChapter(Map<String, Object> chapter) {
        int id = insertMapReturnKey(TABLE_NAME, chapter).intValue();
        return this.getChapter(id);
    }

    @Override
    public Map<String, Object> getLastChapterByCourseIdAndType(Object courseId, String type) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE  courseId = ? AND type = ? ORDER BY seq DESC LIMIT 1";
        return getJdbcTemplate().queryForList(sql, courseId, type).stream().findFirst().orElse(new HashMap<>(0));
    }

    @Override
    public int getChapterCountByCourseIdAndTypeAndParentId(Object courseId, String type, int parentId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE  courseId = ? AND type = ? AND parentId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, courseId, type, parentId);
    }

    @Override
    public int getChapterCountByCourseIdAndType(Object courseId, String type) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE  courseId = ? AND type = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, courseId, type);
    }

    @Override
    public int getChapterMaxSeqByCourseId(Object courseId) {
        String sql = "SELECT MAX(seq) FROM " + TABLE_NAME + " WHERE  courseId = ?";
        int val;
        try {
            val = getJdbcTemplate().queryForObject(sql, Integer.class, courseId);
        } catch (Exception e) {
            val = 0;
        }
        return val;
    }
}
