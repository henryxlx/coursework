package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface LessonLearnDao {

    Integer findLearnsCountByLessonId(Integer lessonId);

    List<Map<String, Object>> findLearnsByLessonId(Integer lessonId, Integer start, Integer limit);

    Integer getLearnCountByUserIdAndCourseIdAndStatus(Object userId, Object courseId, String status);

    void deleteLearnsByLessonId(Integer lessonId);
}
