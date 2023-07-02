package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.service.testpaper.TestPaperExamResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
public interface TestPaperService extends FastEventService {

    Map<String, Object> getTestpaper(Object id);

    Integer searchTestpapersCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchTestpapers(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    Integer createTestpaper(Map<String, Object> fields);

    void deleteTestpaper(Object id);

    void updateTestpaperItems(Object testpaperId, List<Map<String, Object>> items);

    List<Map<String, Object>> getTestpaperItems(Object testpaperId);

    void updateTestpaper(Integer testpaperId, Map<String, Object> fields);

    List<Map<String, Object>> buildTestpaper(Object id, Map<String, Object> options);

    Map<String, Object> publishTestpaper(Object testpaperId);

    Map<String, Object> closeTestpaper(Integer testpaperId);

    Map<String, Map<String, Object>> previewTestpaper(Integer testpaperId);

    Map<String, Object> findTestpaperResultByTestpaperIdAndUserIdAndActive(Integer testpaperId, Integer userId);

    Integer findTestpaperResultsCountByUserId(Integer userId);

    List<Map<String, Object>> findTestpaperResultsByUserId(Integer userId, Integer start, Integer limit);

    List<Map<String, Object>> findTestpapersByIds(Set<Object> ids);

    Map<String, Object> startTestpaper(Integer testId, Map<String, Object> options);

    Map<String, Object> getTestpaperResult(Integer id);

    default TestPaperExamResult showTestpaper(Integer testpaperId) {
        return this.showTestpaper(testpaperId, false);
    }

    TestPaperExamResult showTestpaper(Integer testpaperId, boolean isAccuracy);

    List<Map<String, Object>> findAllTestpapersByTarget(Integer id);

    Integer findTestpaperResultCountByStatusAndTestIds(Set<Object> testpaperIds, String status);

    List<Map<String, Object>> findTestpaperResultsByStatusAndTestIds(Set<Object> testpaperIds, String status,
                                                                     Integer start, Integer limit);

    boolean canTeacherCheck(Object testpaperId, AppUser currentUser);

    List<Map<String, Object>> submitTestpaperAnswer(Integer testpaperId, Map<String, Object> answers, AppUser user);

    void updateTestpaperResult(Integer testpaperId, Object usedTime);

    List<Map<String, Object>> makeTestpaperResultFinish(Integer id, AppUser currentUser);

    Map<String, Object> finishTest(Integer id, Integer userId, Object usedTime);

    boolean isExistsEssay(List<Map<String, Object>> itemResults);

    List<Map<String, Object>> findAllTestpapersByTargets(Set<Object> courseIds);
}
