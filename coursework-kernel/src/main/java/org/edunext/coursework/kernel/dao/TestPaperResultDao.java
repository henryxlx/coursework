package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
public interface TestPaperResultDao {

    Map<String, Object> addTestpaperResult(Map<String, Object> fields);

    Map<String, Object> getTestpaperResult(Integer id);

    Integer findTestpaperResultsCountByUserId(Integer userId);

    List<Map<String, Object>> findTestpaperResultsByUserId(Integer userId, Integer start, Integer limit);

    Map<String, Object> findTestpaperResultByTestpaperIdAndUserIdAndActive(Integer testpaperId, Integer userId);

    Integer findTestpaperResultCountByStatusAndTestIds(Set<Object> testpaperIds, String status);

    List<Map<String, Object>> findTestpaperResultsByStatusAndTestIds(Set<Object> testpaperIds, String status, Integer start, Integer limit);

    int updateTestpaperResultActive(Object testId, Object userId);

    int updateTestpaperResult(Integer testpaperId, Map<String, Object> fields);

    Map<String, Object> findTestpaperResultsByTestIdAndStatusAndUserId(Integer testpaperId, String[] status, Integer userId);

    Integer searchTestpapersScore(Map<String, Object> conditions);

    Integer searchTestpaperResultsCount(Map<String, Object> conditions);
}
