package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
public interface TestPaperItemResultDao {

    List<Map<String, Object>> findTestResultsByTestpaperResultId(Integer testPaperResultId);

    List<Map<String, Object>> findTestResultsByItemIdAndTestId(Set<String> questionIds, Object testPaperResultId);

    void addItemAnswers(Object testPaperResultId, Map<String, Object> answers, Object testPaperId, Integer userId);

    void updateItemAnswers(Object testPaperResultId, Map<String, Object> answers);

    void addItemResult(Map<String, Object> answer);

    void updateItemResults(Map<String, Map<String, Object>> answers, Object testPaperResultId);

    void updateItemEssays(Map<String, Map<String, Object>> testResults, Integer id);
}
