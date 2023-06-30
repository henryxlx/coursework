package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
public interface TestPaperDao {

    Integer searchTestpapersCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchTestpapers(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    Integer addTestpaper(Map<String, Object> fields);

    Map<String, Object> getTestpaper(Object id);

    int deleteTestpaper(Object id);

    void updateTestpaper(Object id, Map<String, Object> fields);

    List<Map<String, Object>> findTestpapersByIds(Set<Object> ids);

    List<Map<String, Object>> findTestpaperByTargets(String... targets);
}
