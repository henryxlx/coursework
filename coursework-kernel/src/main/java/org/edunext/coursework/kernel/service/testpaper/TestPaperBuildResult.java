package org.edunext.coursework.kernel.service.testpaper;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public class TestPaperBuildResult {

    private String status;

    private List<Map<String, Object>> items;

    private Map<String, Object> missingMap;

    public String getStatus() {
        return status;
    }

    public TestPaperBuildResult setStatus(String status) {
        this.status = status;
        return this;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public TestPaperBuildResult setItems(List<Map<String, Object>> items) {
        this.items = items;
        return this;
    }

    public TestPaperBuildResult setMissing(Map<String, Object> map) {
        this.missingMap = map;
        return this;
    }
}
