package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.AppUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
public interface CourseService {

    int searchCourseCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchCourses(Map<String, Object> conditions,
                                            String sort, Integer start, Integer limit);

    Map<String, Object> createCourse(AppUser currentUser, Map<String, Object> fields);

    Map<String, Object> tryManageCourse(AppUser currentUser, Integer id);

    void updateCourse(Integer id, Map<String, Object> fields);

    List<Map<String, Object>> findCoursesByLikeTitle(Object title);

    List<Map<String, Object>> findCoursesByIds(Set<Object> ids);

    List<Map<String, Object>> findLessonsByIds(Set<Object> ids);

    Integer searchMemberCount(Map<String, Object> conditions);

    Integer searchLearnTime(Map<String, Object> conditions);

    Integer searchLessonCount(Map<String, Object> conditions);
}
