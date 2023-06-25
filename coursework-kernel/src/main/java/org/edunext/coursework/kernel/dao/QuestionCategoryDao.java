package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface QuestionCategoryDao {

    List<Map<String, Object>> findCategoriesByTarget(String target, Integer start, Integer limit);
}
