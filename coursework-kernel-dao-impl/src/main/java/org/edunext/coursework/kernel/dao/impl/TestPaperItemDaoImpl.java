package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.TestPaperItemDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class TestPaperItemDaoImpl extends FastJdbcDaoSupport implements TestPaperItemDao {

    static final String TABLE_NAME = "cw_testpaper_item";

    public Map<String, Object> getItem(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(new HashMap<>(0));
    }

    @Override
    public void deleteItemsByTestPaperId(Object testPaperId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE testId = ? ";
        getJdbcTemplate().update(sql, testPaperId);
    }

    @Override
    public Map<String, Object> addItem(Map<String, Object> item) {
        Integer id = insertMapReturnKey(TABLE_NAME, item).intValue();
        return this.getItem(id);
    }

    @Override
    public List<Map<String, Object>> findItemsByTestPaperId(Object testPaperId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE testId = ? order by `seq` asc ";
        return getJdbcTemplate().queryForList(sql, testPaperId);
    }

    @Override
    public Map<String, Object> updateItem(Object id, Map<String, Object> item) {
        updateMap(TABLE_NAME, item, "id", id);
        return getItem(id);
    }

    @Override
    public void deleteItem(Object id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        getJdbcTemplate().update(sql, id);
    }

    @Override
    public List<Map<String, Object>> findItemsByTestpaperId(Object testPaperId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE testId = ? order by `seq` asc ";
        return getJdbcTemplate().queryForList(sql, testPaperId);
    }
}
