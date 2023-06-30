package org.edunext.coursework.kernel.service.testpaper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xulixin
 */
public class TestPaperExamResult {

    Map<String, Map<String, Object>> formatItems = new HashMap<>(0);

    Map<String, Map<String, Integer>> accuracy = new HashMap<>(0);

    public Map<String, Map<String, Object>> getFormatItems() {
        return formatItems;
    }

    public void setFormatItems(Map<String, Map<String, Object>> formatItems) {
        this.formatItems = formatItems;
    }

    public Map<String, Map<String, Integer>> getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Map<String, Map<String, Integer>> accuracy) {
        this.accuracy = accuracy;
    }
}
