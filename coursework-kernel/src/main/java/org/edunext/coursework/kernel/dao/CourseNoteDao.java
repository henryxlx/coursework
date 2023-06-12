package org.edunext.coursework.kernel.dao;

/**
 * @author xulixin
 */
public interface CourseNoteDao {

    Integer getNoteCountByUserIdAndCourseId(Integer userId, Integer courseId);
}
