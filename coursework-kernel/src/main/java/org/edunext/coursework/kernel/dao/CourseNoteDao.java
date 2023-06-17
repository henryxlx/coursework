package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseNoteDao {

    Integer getNoteCountByUserIdAndCourseId(Integer userId, Integer courseId);

    Map<String, Object> getNoteByUserIdAndLessonId(Integer userId, Integer lessonId);

    void addNote(Map<String, Object> note);

    void updateNote(Object id, Map<String, Object> note);

    Integer searchNoteCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchNotes(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    void deleteNote(Integer id);

    Map<String, Object> getNote(Object id);
}
