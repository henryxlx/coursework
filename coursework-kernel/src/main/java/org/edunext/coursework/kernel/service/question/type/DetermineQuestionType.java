package org.edunext.coursework.kernel.service.question.type;

import com.jetwinner.util.FastHashMap;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public class DetermineQuestionType extends AbstractQuestionType {

    private Object getFirstQuestionAnswer(Map<String, Object> question) {
        List<Object> questionAnswers = (List<Object>) question.get("answer");
        return questionAnswers != null ? questionAnswers.get(0) : null;
    }

    @Override
    public Map<String, Object> judge(Map<String, Object> question, Object[] answer) {
        Object rightAnswer = getFirstQuestionAnswer(question);
        Object userAnswer = arrayPop(answer);

        String status = equals(userAnswer, rightAnswer) ? "right" : "wrong";

        return FastHashMap.build(1).add("status", status).toMap();
    }
}
