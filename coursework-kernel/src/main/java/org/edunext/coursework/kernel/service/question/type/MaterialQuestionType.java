package org.edunext.coursework.kernel.service.question.type;

import com.jetwinner.util.FastHashMap;

import java.util.Map;

/**
 * @author xulixin
 */
public class MaterialQuestionType extends AbstractQuestionType {

    @Override
    public Map<String, Object> judge(Map<String, Object> question, Object[] answer) {
        return FastHashMap.build(1).add("status", "none").toMap();
    }

    @Override
    public boolean canHaveSubQuestion() {
        return true;
    }
}
