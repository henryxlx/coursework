package org.edunext.coursework.kernel.service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseThreadService {

    int searchThreadCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchThreads(Map<String, Object> conditions, String sort, Integer start, Integer limit);

    Map<String, Object> createThread(Map<String, Object> fields);

    int searchThreadCountInCourseIds(Map<String, Object> conditions);

    List<Map<String, Object>> searchThreadInCourseIds(Map<String, Object> conditions, String order,
                                                      Integer start, Integer limit);
}
