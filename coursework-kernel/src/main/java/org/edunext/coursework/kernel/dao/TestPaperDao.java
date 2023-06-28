package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;

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
}
