package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface TestPaperService {

    Map<String, Object> getTestpaper(Object id);

    Integer searchTestpapersCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchTestpapers(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    Integer createTestpaper(Map<String, Object> fields);

    void deleteTestpaper(Object id);

    void updateTestpaperItems(Object testpaperId, List<Map<String, Object>> items);

    List<Map<String, Object>> getTestpaperItems(Object testpaperId);

    void updateTestpaper(Integer testpaperId, Map<String, Object> fields);

    List<Map<String, Object>> buildTestpaper(Object id, Map<String, Object> options);

    Map<String, Map<String, Object>> previewTestpaper(Integer testId);

    Map<String, Object> publishTestpaper(Object testpaperId);

    Map<String, Object> closeTestpaper(Integer testpaperId);

    Map<String, Object> findTestpaperResultByTestpaperIdAndUserIdAndActive(Integer testpaperId, AppUser user);
}
