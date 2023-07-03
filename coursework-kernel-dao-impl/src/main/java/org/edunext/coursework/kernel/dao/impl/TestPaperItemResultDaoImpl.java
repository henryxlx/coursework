package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.JsonUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.TestPaperItemResultDao;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xulixin
 */
@Repository
public class TestPaperItemResultDaoImpl extends FastJdbcDaoSupport implements TestPaperItemResultDao {

    static final String TABLE_NAME = "cw_testpaper_item_result";

    private void unserialize(Map<String, Object> fields) {
        if (EasyStringUtil.isNotBlank(fields.get("answer"))) {
            fields.put("answer", JsonUtil.jsonDecodeMap(fields.get("answer")));
        }
    }

    private void serialize(Map<String, Object> fields) {
        if (EasyStringUtil.isNotBlank(fields.get("answer"))) {
            fields.put("answer", JsonUtil.objectToString(fields.get("answer")));
        }
    }

    @Override
    public List<Map<String, Object>> findTestResultsByTestpaperResultId(Integer testPaperResultId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE testPaperResultId = ?";
        List<Map<String, Object>> results = getJdbcTemplate().queryForList(sql, testPaperResultId);
        results.forEach(this::unserialize);
        return results;
    }

    @Override
    public List<Map<String, Object>> findTestResultsByItemIdAndTestId(Set<String> questionIds, Object testPaperResultId) {
        if (questionIds == null || questionIds.size() < 1) {
            return new ArrayList<>(0);
        }
        String marks = questionIds.stream().collect(Collectors.joining(", "));
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE questionId IN (" + marks + ") AND testPaperResultId = ?";
        return getJdbcTemplate().queryForList(sql, testPaperResultId);
    }

    @Override
    public void addItemAnswers(Object testPaperResultId, Map<String, Object> answers, Object testPaperId, Integer userId) {
        if (MapUtil.isEmpty(answers)) {
            return;
        }

        List<String> keys = new ArrayList<>(answers.keySet());
        keys.forEach(k -> answers.put(k, JsonUtil.objectToString(answers.get(k))));

        String sql = "INSERT INTO " + TABLE_NAME + " (`testId`, `testPaperResultId`, `userId`, `questionId`, `answer`) VALUES (?, ?, ?, ?, ?);";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, testPaperId);
                ps.setObject(2, testPaperResultId);
                ps.setObject(3, userId);
                ps.setObject(4, keys.get(i));
                ps.setObject(5, answers.get(keys.get(i)));
            }

            @Override
            public int getBatchSize() {
                return keys.size();
            }
        });
    }

    @Override
    public void updateItemAnswers(Object testPaperResultId, Map<String, Object> answers) {
        if (MapUtil.isEmpty(answers)) {
            return;
        }

        List<String> keys = new ArrayList<>(answers.keySet());
        keys.forEach(k -> answers.put(k, JsonUtil.objectToString(answers.get(k))));

        String sql = "UPDATE " + TABLE_NAME + " set `answer` = ? WHERE `questionId` = ? AND `testPaperResultId` = ?;";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, answers.get(keys.get(i)));
                ps.setObject(2, keys.get(i));
                ps.setObject(3, testPaperResultId);
            }

            @Override
            public int getBatchSize() {
                return keys.size();
            }
        });
    }

    @Override
    public void addItemResult(Map<String, Object> answer) {
        this.serialize(answer);
        insertMap(TABLE_NAME, answer);
    }

    @Override
    public void updateItemResults(Map<String, Map<String, Object>> answers, Object testPaperResultId) {
        if (MapUtil.isEmpty(answers)) {
            return;
        }

        List<String> keys = new ArrayList<>(answers.keySet());

        String sql = "UPDATE " + TABLE_NAME + " set `status` = ?, `score` = ? WHERE `questionId` = ? AND `testPaperResultId` = ?;";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> value = answers.get(keys.get(i));
                ps.setObject(1, value.get("status"));
                ps.setObject(2, value.get("score"));
                ps.setObject(3, keys.get(i));
                ps.setObject(4, testPaperResultId);
            }

            @Override
            public int getBatchSize() {
                return keys.size();
            }
        });
    }

    @Override
    public void updateItemEssays(Map<String, Map<String, Object>> answers, Integer testPaperResultId) {
        if (MapUtil.isEmpty(answers)) {
            return;
        }

        List<String> keys = new ArrayList<>(answers.keySet());

        String sql = "UPDATE " + TABLE_NAME + " set `score` = ?, `teacherSay` = ?, `status` = ? WHERE `questionId` = ? AND `testPaperResultId` = ?;";

        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> value = answers.get(keys.get(i));
                ps.setObject(1, value.get("score"));
                ps.setObject(2, value.get("teacherSay"));
                ps.setObject(3, value.get("status"));
                ps.setObject(4, keys.get(i));
                ps.setObject(5, testPaperResultId);
            }

            @Override
            public int getBatchSize() {
                return keys.size();
            }
        });
    }
}
