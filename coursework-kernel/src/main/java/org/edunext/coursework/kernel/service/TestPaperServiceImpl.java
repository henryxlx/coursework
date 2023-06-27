package org.edunext.coursework.kernel.service;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.dao.TestPaperDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class TestPaperServiceImpl implements TestPaperService {

    private final TestPaperDao testPaperDao;

    public TestPaperServiceImpl(TestPaperDao testPaperDao) {
        this.testPaperDao = testPaperDao;
    }

    @Override
    public Map<String, Object> getTestpaper(Object id) {
        return this.testPaperDao.getTestpaper(id);
    }

    @Override
    public Integer searchTestpapersCount(Map<String, Object> conditions) {
        return this.testPaperDao.searchTestpapersCount(conditions);
    }

    @Override
    public List<Map<String, Object>> searchTestpapers(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        return this.testPaperDao.searchTestpapers(conditions, orderBy, start, limit);
    }

    @Override
    public Integer createTestpaper(Map<String, Object> fields) {
        return this.testPaperDao.addTestpaper(this.filterTestpaperFields(fields, "create"));
    }

    @Override
    public void deleteTestpaper(Object id) {
        this.testPaperDao.deleteTestpaper(id);
        // this.testPaperItemDao.deleteItemsByTestpaperId(id);
    }

    private Map<String, Object> filterTestpaperFields(Map<String, Object> fields) {
        return filterTestpaperFields(fields, "create");
    }

    private Map<String, Object> filterTestpaperFields(Map<String, Object> fields, String mode) {
        Map<String, Object> filterMap = new HashMap<>(12);
        Integer currentUserId = AppUser.getCurrentUser(fields).getId();
        filterMap.put("updatedUserId", currentUserId);
        filterMap.put("updatedTime", System.currentTimeMillis());
        if ("create".equals(mode)) {
            if (!ArrayToolkit.required(fields, "name", "pattern", "target")) {
                throw new RuntimeGoingException("缺少必要字段！");
            }
            filterMap.put("name", fields.get("name"));
            filterMap.put("target", fields.get("target"));
            filterMap.put("pattern", fields.get("pattern"));
            filterMap.put("description", EasyStringUtil.isBlank(fields.get("description")) ? "" : fields.get("description"));
            filterMap.put("limitedTime", fields.get("limitedTime") == null ? 0 : fields.get("limitedTime"));
            filterMap.put("metas", fields.get("metas") == null ? new HashMap<>(0) : fields.get("metas"));
            filterMap.put("status", "draft");
            filterMap.put("createdUserId", currentUserId);
            filterMap.put("createdTime", System.currentTimeMillis());
        } else {
            if (fields.containsKey("name")) {
                filterMap.put("name", EasyStringUtil.isBlank(fields.get("name")) ? "" : fields.get("name"));
            }

            if (fields.containsKey("description")) {
                filterMap.put("description", EasyStringUtil.isBlank(fields.get("description")) ? "" : fields.get("description"));
            }

            if (fields.containsKey("limitedTime")) {
                filterMap.put("limitedTime", ValueParser.parseInt(fields.get("limitedTime")));
            }

            if (fields.containsKey("passedScore")) {
                filterMap.put("passedScore", ValueParser.parseFloat(fields.get("passedScore")));
            }
        }

        return filterMap;
    }
}
