package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface LessonDao {

    List<Map<String, Object>> findLessonsByCourseId(Object courseId);

    int getLessonMaxSeqByCourseId(Object courseId);

    List<Map<String, Object>> findLessonsByChapterId(Object chapterId);

    void batchUpdateLesson(List<Map<String, Object>> lessons, String pKeyName, String... updateFieldNames);
}
