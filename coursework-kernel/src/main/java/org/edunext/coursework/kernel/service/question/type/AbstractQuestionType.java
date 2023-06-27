package org.edunext.coursework.kernel.service.question.type;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author xulixin
 */
public abstract class AbstractQuestionType {

    abstract public Map<String, Object> judge(Map<String, Object> question, Object[] answer);

    public boolean canHaveSubQuestion() {
        return false;
    }

    public boolean hasMissScore() {
        return false;
    }

    public Map<String, Object> filter(Map<String, Object> fields) {
        return this.filter(fields, "create");
    }

    public Map<String, Object> filter(Map<String, Object> fields, String mode) {
        return this.commonFilter(fields, mode);
    }

    protected Map<String, Object> commonFilter(Map<String, Object> fields, String mode) {
        Map<String, Object> filtered = new HashMap<>();
        filtered.put("type", fields.get("type"));
        filtered.put("stem", EasyStringUtil.isBlank(fields.get("stem")) ? "" : EasyStringUtil.purifyHtml(fields.get("stem")));
        filtered.put("difficulty", EasyStringUtil.isBlank(fields.get("difficulty")) ? "simple" : fields.get("difficulty"));
        filtered.put("userId", AppUser.getCurrentUser(fields).getId());
        filtered.put("answer", fields.get("answer") == null ? new ArrayList<>(0) : fields.get("answer"));
        filtered.put("analysis", EasyStringUtil.isBlank(fields.get("analysis")) ? null : fields.get("analysis"));
        filtered.put("metas", fields.get("metas") == null ? new HashMap(0) : fields.get("metas"));
        filtered.put("score", ValueParser.parseInt(fields.get("score")));
        filtered.put("categoryId", ValueParser.parseInt(fields.get("categoryId")));
        filtered.put("parentId", ValueParser.parseInt(fields.get("parentId")));
        if ("update".equals(mode)) {
            filtered.remove("parentId");
        }

        filtered.put("updatedTime", System.currentTimeMillis());
        if ("create".equals(mode)) {
            filtered.put("createdTime", System.currentTimeMillis());
        }

        filtered.put("target", EasyStringUtil.isBlank(fields.get("target")) ? "" : fields.get("target"));

        return filtered;
    }

    protected int count(Object mass) {
        if (mass instanceof Array) {
            return ((Object[]) mass).length;
        }
        return mass == null ? 0 : 1;
    }

    protected Object[] arrayDiff(Object[] targets, Object mass) {
        return arrayDiff(mass, targets);
    }

    protected Object[] arrayDiff(Object mass, Object[] targets) {
        Object[] arrayForMass = null;
        if (mass instanceof Array) {
            arrayForMass = (Object[]) mass;
        }
        Set<Object> diff = new HashSet<>();
        if (arrayForMass != null) {
            for (Object objMass : arrayForMass) {
                boolean notFound = true;
                for (Object objTarget : targets) {
                    if (equals(objMass, objTarget)) {
                        notFound = false;
                        break;
                    }
                }
                if (notFound) {
                    diff.add(objMass);
                }
            }
        }
        return diff.toArray();
    }

    protected boolean equals(Object source, Object dest) {
        if (source != null || dest != null) {
            String str1 = String.valueOf(source).trim();
            String str2 = String.valueOf(dest).trim();
            return str1.equals(str2);
        }
        return false;
    }

    protected Object arrayPop(Object obj) {
        if (obj instanceof Array) {
            return arrayPop((Object[]) obj);
        }
        return null;
    }

    protected Object arrayPop(Object[] array) {
        if (array != null) {
            int len = array.length;
            if (len > 0) {
                return array[len - 1];
            }
        }
        return null;
    }

    protected Object[] arrayValues(Object mass) {
        if (mass instanceof Array) {
            return (Object[]) mass;
        }
        return new Object[0];
    }
}
