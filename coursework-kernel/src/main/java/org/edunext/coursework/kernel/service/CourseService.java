package org.edunext.coursework.kernel.service;

import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.SetUtil;
import com.jetwinner.webfast.event.FastEventHandler;
import com.jetwinner.webfast.event.ServiceEvent;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.ActionGraspException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xulixin
 */
public interface CourseService {

    default void dispatchEvent(String eventName, ServiceEvent serviceEvent) {
        FastEventHandler.getDefault().dispatchEvent(eventName, serviceEvent);
    }

    class CourseSerialize {

        private static final String UNSERIALIZE_KEY = "unserialize";

        public static String[] objectToArray(Object obj) {
            String[] arr = null;
            if (EasyStringUtil.isNotBlank(obj)) {
                String s = String.valueOf(obj);
                if (s.startsWith("|")) {
                    s = s.substring(1);
                }
                arr = s.split("\\|");
            }
            return arr;
        }

        public static void objectToArray(Map<String, Object> map, String key) {
            Object obj = map.get(key);
            String[] arr = objectToArray(obj);
            if (arr != null) {
                map.put(key, arr);
            }
        }

        public static void unserialize(Map<String, Object> course) {
            if (course == null || course.isEmpty() || course.containsKey(UNSERIALIZE_KEY)) {
                return;
            }

            objectToArray(course, "goals");
            objectToArray(course, "audiences");
            objectToArray(course, "teacherIds");
            course.put(UNSERIALIZE_KEY, Boolean.TRUE);
        }

        public static String listToString(List<?> list) {
            List<String> strList = list.stream().map(String::valueOf).collect(Collectors.toList());
            return strList.stream().collect(Collectors.joining("|", "|", "|"));
        }

        public static void arrayToString(Map<String, Object> model, String key) {
            Object objTarget = model.get(key);
            boolean needSerialize = true;
            if (ArrayUtil.isNotArray(objTarget)) {
                if (EasyStringUtil.isNotBlank(objTarget)) {
                    objTarget = new Object[]{objTarget};
                } else {
                    needSerialize = false;
                }
            }
            if (needSerialize) {
                model.put(key, '|' + EasyStringUtil.implode("|", objTarget) + '|');
            }
        }

        public static void serialize(Map<String, Object> course) {
            arrayToString(course, "goals");
            arrayToString(course, "audiences");
            arrayToString(course, "teacherIds");
        }
    }

    /**
     * 每个课程可添加的最大的教师人数
     */
    Integer MAX_TEACHER = 100;

    int searchCourseCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchCourses(Map<String, Object> conditions,
                                            String sort, Integer start, Integer limit);

    Map<String, Object> createCourse(AppUser currentUser, Map<String, Object> fields);

    Map<String, Object> tryManageCourse(AppUser currentUser, Integer id);

    Map<String, Object> tryAdminCourse(AppUser currentUser, Integer courseId);

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

    Boolean hasFavoritedCourse(Integer courseId, AppUser currentUser);

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

    void favoriteCourse(AppUser user, Integer courseId) throws ActionGraspException;

    void unfavoriteCourse(AppUser user, Integer courseId) throws ActionGraspException;

    Boolean canManageCourse(Integer courseId, Integer userId);

    Map<String, Object> remarkStudent(Integer courseId, Integer userId, String remark);

    void removeStudent(AppUser currentUser, Integer courseId, Integer userId);

    void addMemberExpiryDays(Integer courseId, Integer userId, Integer day);

    boolean isCourseStudent(Integer courseId, Integer userId);

    boolean isCourseTeacher(Integer courseId, Integer userId);

    Map<String, Object> updateLesson(Integer courseId, Integer lessonId, Map<String, Object> fields, AppUser currentUser);

    int deleteLesson(Integer courseId, Integer lessonId, AppUser currentUser);

    void publishLesson(Integer courseId, Integer lessonId, AppUser currentUser);

    void unpublishLesson(Integer courseId, Integer lessonId, AppUser currentUser);

    boolean canTakeCourse(Integer id, Integer userId);

    Map<String, Object> tryTakeCourse(Integer id, AppUser currentUser);

    boolean isMemberNonExpired(Map<String, Object> course, Map<String, Object> member);

    List<Map<String, Object>> getCourseLessonReplayByLessonId(Object lessonId);

    Map<String, Object> canLearnLesson(Integer courseId, Integer lessonId, AppUser currentUser);

    Object getUserLearnLessonStatus(Integer userId, Integer courseId, Integer lessonId);

    Boolean startLearnLesson(Integer courseId, Integer lessonId, AppUser currentUser);

    void waveLearningTime(Integer userId, Integer lessonId, Integer time);

    void finishLearnLesson(Integer courseId, Integer lessonId, AppUser currentUser);

    void cancelLearnLesson(Integer courseId, Integer lessonId, AppUser currentUser);

    Map<String, Object> getUserNextLearnLesson(Integer userId, Integer courseId);

    List<Map<String, Object>> getCourseLessons(Integer courseId);

    boolean setMemberNoteNumber(Integer courseId, Integer userId, Integer number);

    Integer getCourseStudentCount(Integer courseId);

    Map<String, Object> createAnnouncement(Integer courseId, Map<String, Object> fields, AppUser currentUser);
}