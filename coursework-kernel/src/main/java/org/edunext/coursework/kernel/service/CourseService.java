package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;

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

    Map<String, Object> getCourse(Integer id);

    List<Map<String, Object>> searchLessons(Map<String, Object> conditions, OrderBy orderBy, int start, int limit);

    List<Map<String, Object>> getCourseItems(Object id);

    Map<String, Object> getCourseMember(Object courseId, Integer userId);

    void hitCourse(Integer id);

    Map<String, Object> getUserLearnLessonStatuses(Integer userId, Object courseId);

    Object hasFavoritedCourse(Object id);

    void recommendCourse(AppUser currentUser, Integer courseId, String number);

    void cancelRecommendCourse(AppUser currentUser, Integer courseId);

    void publishCourse(AppUser currentUser, Integer courseId);

    void closeCourse(AppUser currentUser, Integer courseId);
}
