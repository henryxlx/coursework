package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.QuestionCategoryDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class QuestionCategoryDaoImpl extends FastJdbcDaoSupport implements QuestionCategoryDao {

    static final String TABLE_NAME = "cw_question_category";

    @Override
    public List<Map<String, Object>> findCategoriesByTarget(String target, Integer start, Integer limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE target = ? ORDER BY seq ASC LIMIT " +
                start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, target);
    }

}
