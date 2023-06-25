package org.edunext.coursework.kernel.service.question.type;

import com.jetwinner.util.FastHashMap;

import java.util.Map;

/**
 * @author xulixin
 */
public class DetermineQuestionType extends AbstractQuestionType {

    @Override
    public Map<String, Object> judge(Map<String, Object> question, Object[] answer) {
        Object rightAnswer = arrayPop(question.get("answer"));
        Object userAnswer = arrayPop(answer);

        String status = equals(userAnswer, rightAnswer) ? "right" : "wrong";

        return FastHashMap.build(1).add("status", status).toMap();
    }
}
