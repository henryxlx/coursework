package org.edunext.coursework.kernel.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ThreadServiceImpl implements ThreadService {

    @Override
    public int searchThreadCount(Map<String, Object> conditions) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> searchThreads(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        return new ArrayList<>();
    }
}
