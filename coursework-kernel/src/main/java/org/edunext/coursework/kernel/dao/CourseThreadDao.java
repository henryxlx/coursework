package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseThreadDao {

    Map<String, Object> getThread(Object id);

    Map<String, Object> addThread(Map<String, Object> thread);

    Integer searchThreadCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchThreads(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    void waveThread(Integer threadId, String field, Integer diff);

    Integer searchThreadCountInCourseIds(Map<String, Object> conditions);

    List<Map<String, Object>> searchThreadInCourseIds(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    void updateThread(Object id, Map<String, Object> fields);
}
