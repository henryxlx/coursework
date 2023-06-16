package org.edunext.coursework.kernel.service;

import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseNoteService {

    Map<String, Object> getUserLessonNote(Integer userId, Integer lessonId);

    void saveNote(Map<String, Object> fields);
}
