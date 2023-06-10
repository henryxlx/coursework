package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
public interface LessonDao {

    Map<String, Object> getLesson(Object id);

    List<Map<String, Object>> findLessonsByCourseId(Object courseId);

    int getLessonMaxSeqByCourseId(Object courseId);

    List<Map<String, Object>> findLessonsByChapterId(Object chapterId);

    void batchUpdateLesson(List<Map<String, Object>> lessons, String pKeyName, String... updateFieldNames);

    Map<String, Object> addLesson(Map<String, Object> lesson);

    Integer getLessonCountByCourseId(Object courseId);

    Integer sumLessonGiveCreditByCourseId(Object courseId);

    void updateLesson(Integer id, Map<String, Object> fields);

    List<Map<String, Object>> findLessonsByIds(Set<Object> ids);

    Integer searchLessonCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchLessons(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);
}
