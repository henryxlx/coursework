package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface TestPaperResultDao {

    Map<String, Object> addTestpaperResult(Map<String, Object> fields);

    Map<String, Object> getTestpaperResult(Integer id);

    Integer findTestpaperResultsCountByUserId(Integer userId);

    List<Map<String, Object>> findTestpaperResultsByUserId(Integer userId, Integer start, Integer limit);

    Map<String, Object> findTestpaperResultByTestpaperIdAndUserIdAndActive(Integer testpaperId, Integer userId);
}
