package org.edunext.coursework.kernel.service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseThreadService {

    int searchThreadCountInCourseIds(Map<String, Object> conditions);

    List<Map<String, Object>> searchThreadInCourseIds(Map<String, Object> conditions, String order,
                                                      Integer start, Integer limit);
}
