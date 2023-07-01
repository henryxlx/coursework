package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

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

    Integer searchLearnTime(Map<String, Object> conditions);

    List<Map<String, Object>> findLearnsByUserIdAndCourseId(Integer userId, Object courseId);

    Integer searchLearnCount(Map<String, Object> conditions);

    Integer searchWatchTime(Map<String, Object> conditions);

    List<Map<String, Object>> searchLearns(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);
}
