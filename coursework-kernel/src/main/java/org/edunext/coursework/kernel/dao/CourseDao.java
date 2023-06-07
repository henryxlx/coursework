package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CourseDao {

    int searchCourseCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchCourses(Map<String, Object> conditions,
                                            OrderBy orderBy, Integer start, Integer limit);

    Map<String, Object> addCourse(Map<String, Object> course);

    Map<String, Object> getCourse(Object id);

    int updateCourse(Integer id, Map<String, Object> fields);

    int deleteCourse(Integer courseId);

    List<Map<String, Object>> findCoursesByLikeTitle(Object title);

    List<Map<String, Object>> findCoursesByIds(Set<Object> ids);

    void waveCourse(Integer id, String field, Integer diff);
}
