package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class TestPaperServiceImpl implements TestPaperService {

    @Override
    public Integer searchTestpapersCount(Map<String, Object> conditions) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> searchTestpapers(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        return null;
    }

    @Override
    public Map<String, Object> createTestpaper(Map<String, Object> fields) {
        return null;
    }
}
