package org.edunext.coursework.kernel.dao;

import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseDraftDao {

    int deleteCourseDrafts(Integer courseId, Integer lessonId, Integer userId);

    Map<String, Object> findCourseDraft(Object courseId, Integer lessonId, Integer userId);
}
