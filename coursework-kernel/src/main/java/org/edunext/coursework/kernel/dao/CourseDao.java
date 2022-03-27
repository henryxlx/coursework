package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderByBuilder;

import java.util.List;
import java.util.Map;

public interface CourseDao {

    int searchCourseCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchCourses(Map<String, Object> conditions,
                                            OrderByBuilder orderByBuilder, Integer start, Integer limit);

    Map<String, Object> addCourse(Map<String, Object> course);

    Map<String, Object> getCourse(Object id);
}