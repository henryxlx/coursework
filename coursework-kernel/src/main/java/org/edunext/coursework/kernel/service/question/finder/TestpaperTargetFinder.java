package org.edunext.coursework.kernel.service.question.finder;

import com.jetwinner.util.FastHashMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Component("testpaperTargetFinder")
@Lazy
public class TestpaperTargetFinder extends AbstractTargetFinder {

    @Override
    public Map<String, Map<String, Object>> find(Set<Object> ids) {
//        List<Map<String, Object>> testpapers = this.testpaperService.findTestpapersByIds(ids);
        List<Map<String, Object>> testpapers = null;

        Map<String, Map<String, Object>> targets = null;
        if (testpapers != null && testpapers.size() > 0) {
            targets = new HashMap<>(testpapers.size());
            for (Map<String, Object> testpaper : testpapers) {
                String id = String.valueOf(testpaper.get("id"));
                targets.put(id, FastHashMap.build(6)
                        .add("type", "testpaper")
                        .add("id", id)
                        .add("simple_name", testpaper.get("name"))
                        .add("name", testpaper.get("name"))
                        .add("full_name", testpaper.get("name"))
                        .add("url", "").toMap());
            }
        }
        return targets == null ? new HashMap<>(0) : targets;
    }
}
