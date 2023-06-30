package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.TestPaperResultDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xulixin
 */
@Repository
public class TestPaperResultDaoImpl extends FastJdbcDaoSupport implements TestPaperResultDao {

    static final String TABLE_NAME = "cw_testpaper_result";

    @Override
    public Map<String, Object> addTestpaperResult(Map<String, Object> fields) {
        Integer id = insertMapReturnKey(TABLE_NAME, fields).intValue();
        return getTestpaperResult(id);
    }

    @Override
    public Map<String, Object> getTestpaperResult(Integer id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
    }

    @Override
    public Integer findTestpaperResultsCountByUserId(Integer userId) {
        String sql = "SELECT COUNT(id) FROM " + TABLE_NAME + " WHERE `userId` = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, userId);
    }

    @Override
    public List<Map<String, Object>> findTestpaperResultsByUserId(Integer userId, Integer start, Integer limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE `userId` = ? ORDER BY beginTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, userId);
    }

    @Override
    public Map<String, Object> findTestpaperResultByTestpaperIdAndUserIdAndActive(Integer testpaperId, Integer userId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE testId = ? AND userId = ? AND active = 1";
        return getJdbcTemplate().queryForList(sql, testpaperId, userId).stream().findFirst().orElse(null);
    }

    @Override
    public Integer findTestpaperResultCountByStatusAndTestIds(Set<Object> testpaperIds, String status) {
        if (testpaperIds == null || testpaperIds.size() < 1) {
            return 0;
        }
        String marks = testpaperIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
        String sql = "SELECT COUNT(id) FROM " + TABLE_NAME + " WHERE `testId` IN (" + marks + ") AND `status` = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, status);
    }

    @Override
    public List<Map<String, Object>> findTestpaperResultsByStatusAndTestIds(Set<Object> testpaperIds, String status,
                                                                            Integer start, Integer limit) {

        if (testpaperIds == null || testpaperIds.size() < 1) {
            return new ArrayList<>(0);
        }
        String marks = testpaperIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
        String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE `testId` IN (" + marks + ") AND `status` = ? ORDER BY endTime DESC LIMIT " +
                start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, status);
    }
}
