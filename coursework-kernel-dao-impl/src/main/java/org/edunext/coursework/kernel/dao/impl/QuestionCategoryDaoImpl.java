package org.edunext.coursework.kernel.dao.impl;

import org.edunext.coursework.kernel.dao.QuestionCategoryDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class QuestionCategoryDaoImpl implements QuestionCategoryDao {

    @Override
    public List<Map<String, Object>> findCategoriesByTarget(String target, Integer start, Integer limit) {
        return null;
    }
}
