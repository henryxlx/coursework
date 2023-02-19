package org.edunext.coursework.kernel.service;

import java.util.List;
import java.util.Map;

public interface ThreadService {

    int searchThreadCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchThreads(Map<String, Object> conditions, String sort, Integer start, Integer limit);
}
