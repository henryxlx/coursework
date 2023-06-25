package org.edunext.coursework.kernel.service.question.finder;

import com.jetwinner.util.FastHashMap;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Component
public class TargetHelperBean {

    private final ApplicationContext applicationContext;

    public TargetHelperBean(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public Map<String, Map<String, Object>> getTargets(Set<Object> targetIds) {
        Map<String, Map<String, Object>> targets = parseTargets(targetIds);

        Map<String, Map<String, Map<String, Object>>> datas = this.loadTargetDatas(targets);

        Set<String> keys = targets.keySet();
        for (String key : keys) {
            Map<String, Object> target = targets.get(key);
            String targetType = String.valueOf(target.get("type"));
            String targetId = String.valueOf(target.get("id"));
            if (datas.get(targetType) == null || datas.get(targetType).get(targetId) == null) {
                targets.put(key, null);
            } else {
                targets.put(key, datas.get(targetType).get(targetId));
            }
        }
        return targets;
    }

    public Map<String, Map<String, Map<String, Object>>> loadTargetDatas(Map<String, Map<String, Object>> targets) {
        Map<String, Set<Object>> groupedTargets = new HashMap<>();
        for (Map<String, Object> target : targets.values()) {
            if ("unknow".equals(target.get("type"))) {
                continue;
            }
            String type = String.valueOf(target.get("type"));
            if (!groupedTargets.containsKey(type)) {
                groupedTargets.put(type, new HashSet<>());
            }
            groupedTargets.get(type).add(target.get("id"));
        }

        Map<String, Map<String, Map<String, Object>>> datas = new HashMap<>();
        Set<String> types = groupedTargets.keySet();
        for (String type : types) {
            Set<Object> ids = groupedTargets.get(type);
            AbstractTargetFinder finder = getFinderByType(type);
            if (finder != null) {
                datas.put(type, finder.find(ids));
            }
        }
        return datas;
    }

    private AbstractTargetFinder getFinderByType(String type) {
        AbstractTargetFinder finder = null;
        if (this.applicationContext != null) {
            finder = this.applicationContext.getBean(type + "TargetFinder", AbstractTargetFinder.class);
        }
        return finder;
    }

    public Map<String, Map<String, Object>> parseTargets(Set<Object> targets) {
        Map<String, Map<String, Object>> parsedTargets = new HashMap<>(targets != null ? targets.size() : 0);
        if (targets != null) {
            for (Object objTarget : targets) {
                String target = String.valueOf(objTarget);
                String[] explodedTarget = target.split("/");
                String lastTarget = explodedTarget != null && explodedTarget.length > 0 ?
                        explodedTarget[explodedTarget.length - 1] : "";

                if (lastTarget.indexOf("-") == -1) {
                    parsedTargets.put(target, FastHashMap.build(2).add("type", "unknow").add("id", 0).toMap());
                } else {
                    String[] arr = lastTarget.split("-");
                    if (arr != null && arr.length == 0) {
                        String type = arr[0];
                        String id = arr[1];
                        parsedTargets.put(target, FastHashMap.build(2).add("type", type).add("id", id).toMap());
                    }
                }
            }
        }

        return parsedTargets;
    }
}
