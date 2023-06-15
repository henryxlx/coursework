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

    Map<String, Object> getLearnByUserIdAndLessonId(Integer userId, Integer lessonId);

    void addLearn(Map<String, Object> fields);

    void updateLearn(Object id, Map<String, Object> fields);

    List<Map<String, Object>> findLearnsByUserIdAndCourseIdAndStatus(Object userId, Integer courseId, String status);
}
