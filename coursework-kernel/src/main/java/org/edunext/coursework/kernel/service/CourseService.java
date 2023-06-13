package org.edunext.coursework.kernel.service;

import com.jetwinner.util.SetUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.ActionGraspException;

import java.util.*;

/**
 * @author xulixin
 */
public interface CourseService {

    /**
     * 每个课程可添加的最大的教师人数
     */
    Integer MAX_TEACHER = 100;

    int searchCourseCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchCourses(Map<String, Object> conditions,
                                            String sort, Integer start, Integer limit);

    Map<String, Object> createCourse(AppUser currentUser, Map<String, Object> fields);

    Map<String, Object> tryManageCourse(AppUser currentUser, Integer id);

    void updateCourse(AppUser currentUser, Integer id, Map<String, Object> fields);

    List<Map<String, Object>> findCoursesByLikeTitle(Object title);

    List<Map<String, Object>> findCoursesByIds(Set<Object> ids);

    List<Map<String, Object>> findLessonsByIds(Set<Object> ids);

    Integer searchMemberCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchMembers(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    Integer searchLearnTime(Map<String, Object> conditions);

    Integer searchLessonCount(Map<String, Object> conditions);

    Map<String, Object> getCourse(Integer id);

    List<Map<String, Object>> searchLessons(Map<String, Object> conditions, OrderBy orderBy, int start, int limit);

    List<Map<String, Object>> getCourseItems(Object id);

    Map<String, Object> getCourseMember(Integer courseId, Integer userId);

    void hitCourse(Integer id);

    Map<String, Object> getUserLearnLessonStatuses(Integer userId, Object courseId);

    Object hasFavoritedCourse(Object id);

    Map<String, Object> recommendCourse(AppUser currentUser, Integer courseId, String number);

    void cancelRecommendCourse(AppUser currentUser, Integer courseId);

    void publishCourse(AppUser currentUser, Integer courseId);

    void closeCourse(AppUser currentUser, Integer courseId);

    boolean deleteCourse(AppUser currentUser, Integer couserId);

    void changeCoursePicture(AppUser currentUser, Object id, String pictureFilePath, Map<String, Object> options);

    int findUserLeaningCourseCount(Integer userId, Map<String, Object> filters);

    List<Map<String, Object>> findUserLeaningCourses(Integer userId, Integer start, Integer limit, Map<String, Object> filters);

    int findUserTeachCourseCount(Integer userId, boolean onlyPublished);

    List<Map<String, Object>> findUserTeachCourses(Integer userId, Integer start, Integer limit, boolean onlyPublished);

    Map<String, Object> createChapter(Map<String, Object> chapter);

    int deleteChapter(Integer courseId, Integer chapterId);

    Map<String, Object> getChapter(Integer courseId, Integer chapterId);

    Map<String, Object> updateChapter(Integer courseId, Integer chapterId, Map<String, Object> fields);

    Map<String, Object> createLesson(Map<String, Object> lesson, AppUser currentUser);

    void deleteCourseDrafts(Integer courseId, Integer lessonId, Integer userId);

    Map<String, Object> findCourseDraft(Object courseId, Integer lessonId, Integer userId);

    void sortCourseItems(Object courseId, String[] itemIds);

    void setCourseTeachers(AppUser currentUser, Integer courseId, List<Map<String, Object>> teachers);

    List<Map<String, Object>> findCourseTeachers(Integer courseId);

    List<Map<String, Object>> searchMember(Map<String, Object> conditions, Integer start, Integer limit);

    List<Map<String, Object>> findCourseStudents(Integer courseId, Integer start, Integer limit);

    @SuppressWarnings("unchecked")
    default Set<Object> getTeacherIds(Object teacherIds) {
        Set<Object> ids = new HashSet<>(0);
        if (teacherIds instanceof Collection) {
            ids = SetUtil.newHashSet((Collection) teacherIds);
        }
        return ids;
    }

    void updateCourseCounter(Integer courseId, Map<String, Object> counter);

    Map<String, Object> getCourseLesson(Integer courseId, Integer lessonId);

    Integer findUserLearnCourseCount(Integer userId);

    List<Map<String, Object>> findUserLearnCourses(Integer userId, Integer start, Integer limit);

    Integer findUserFavoritedCourseCount(Integer userId);

    List<Map<String, Object>> findUserFavoritedCourses(Integer userId, Integer start, Integer limit);

    void becomeStudent(Map<String, Object> course, Integer courseId, Integer userId, Map<String, Object> info) throws ActionGraspException;

    default int findUserLearnedCourseCount(Integer userId) {
        return this.findUserLearnedCourseCount(userId, null);
    }

    int findUserLearnedCourseCount(Integer userId, Map<String, Object> filters);

    default List<Map<String, Object>> findUserLearnedCourses(Integer userId, Integer start, Integer limit) {
        return this.findUserLearnedCourses(userId, start, limit, null);
    }

    List<Map<String, Object>> findUserLearnedCourses(Integer userId, Integer start, Integer limit, Map<String, Object> filters);

    void mergeTeacherIds(Set<Object> userIds, Object teacherIds);

    void favoriteCourse(AppUser user, Integer courseId);

    void unfavoriteCourse(AppUser user, Integer courseId);

    Boolean canManageCourse(Integer courseId, Integer userId);
}
