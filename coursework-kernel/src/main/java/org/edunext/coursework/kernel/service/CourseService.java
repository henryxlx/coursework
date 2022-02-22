package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.AppUser;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseService {

    int searchCourseCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchCourses(Map<String, Object> conditions,
                                            String sort, Integer start, Integer limit);

    Map<String, Object> createCourse(AppUser currentUser, Map<String, Object> fields);

    Map<String, Object> tryManageCourse(Integer id);

    void updateCourse(Integer id, Map<String, Object> fields);
}
