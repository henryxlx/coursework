package org.edunext.coursework.kernel.dao;

import java.util.Map;

/**
 * @author xulixin
 */
public interface LessonViewDao {

    Map<String, Object> getLessonView(Object id);

    Map<String, Object> addLessonView(Map<String, Object> createLessonView);
}
