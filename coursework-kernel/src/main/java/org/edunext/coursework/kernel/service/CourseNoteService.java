package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.AppUser;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseNoteService {

    Map<String, Object> getUserLessonNote(Integer userId, Integer lessonId);

    void saveNote(Map<String, Object> note, AppUser currentUser);

    Integer searchNoteCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchNotes(Map<String, Object> conditions, String sort, Integer start, Integer limit);

    void deleteNote(Integer id, AppUser currentUser);

    void deleteNotes(String[] ids, AppUser currentUser);

    Map<String, Object> getNote(Object id);
}
