package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.JsonUtil;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.TestPaperDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class TestPaperDaoImpl extends FastJdbcDaoSupport implements TestPaperDao {

    static final String TABLE_NAME = "cw_testpaper";

    @Override
    public Integer searchTestpapersCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions).select("COUNT(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchTestpapers(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions).select("*")
                .orderBy(orderBy)
                .setFirstResult(start)
                .setMaxResults(limit);

        List<Map<String, Object>> questions = getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
        questions.forEach(this::unserialize);
        return questions;
    }

    @Override
    public Integer addTestpaper(Map<String, Object> fields) {
        this.serialize(fields);
        return insertMapReturnKey(TABLE_NAME, fields).intValue();
    }

    @Override
    public Map<String, Object> getTestpaper(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        Map<String, Object> testpaper = getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
        return testpaper != null ? this.unserialize(testpaper) : null;
    }

    @Override
    public int deleteTestpaper(Object id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        return getJdbcTemplate().update(sql, id);
    }

    private Map<String, Object> unserialize(Map<String, Object> map) {
        if (map.get("metas") != null) {
            map.put("metas", JsonUtil.jsonDecodeMap(map.get("metas")));
        }
        return map;
    }

    private void serialize(Map<String, Object> fields) {
        fields.put("metas", JsonUtil.objectToString(fields.get("metas")));
    }

    private DynamicQueryBuilder createSearchQueryBuilder(Map<String, Object> conditions) {
        ArrayToolkit.arrayFilter(conditions);

        if (EasyStringUtil.isNotBlank(conditions.get("targetPrefix"))) {
            conditions.put("targetLike", conditions.get("targetPrefix") + "%");
            conditions.remove("target");
        }

        return new DynamicQueryBuilder(conditions)
                .from(TABLE_NAME, "testpaper")
                .andWhere("target = :target")
                .andWhere("target LIKE :targetLike")
                .andWhere("status LIKE :status");
    }
}
