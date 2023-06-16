package org.edunext.coursework.kernel.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class CourseThreadServiceImpl implements CourseThreadService {

    @Override
    public int searchThreadCount(Map<String, Object> conditions) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> searchThreads(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        return null;
    }

    @Override
    public Map<String, Object> createThread(Map<String, Object> fields) {
        return null;
    }

    @Override
    public int searchThreadCountInCourseIds(Map<String, Object> conditions) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> searchThreadInCourseIds(Map<String, Object> conditions, String order,
                                                             Integer start, Integer limit) {

        return null;
    }
}
