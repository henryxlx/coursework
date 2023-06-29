package org.edunext.coursework.kernel.service.testpaper;

import java.util.HashMap;
import java.util.Map;

public class TestPaperExamResult {

    Map<String, Map<String, Object>> formatItems = new HashMap<>(0);

    Map<String, Object> accuracy = new HashMap<>(0);

    public Map<String, Map<String, Object>> getFormatItems() {
        return formatItems;
    }

    public void setFormatItems(Map<String, Map<String, Object>> formatItems) {
        this.formatItems = formatItems;
    }

    public Map<String, Object> getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Map<String, Object> accuracy) {
        this.accuracy = accuracy;
    }
}
