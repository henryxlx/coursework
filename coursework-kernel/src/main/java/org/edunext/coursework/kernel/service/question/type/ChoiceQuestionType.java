package org.edunext.coursework.kernel.service.question.type;

import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.ValueParser;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public class ChoiceQuestionType extends AbstractQuestionType {

    @Override
    public boolean hasMissScore() {
        return true;
    }

    @Override
    public Map<String, Object> filter(Map<String, Object> fields, String mode) {
        if (ValueParser.parseInt(fields.get("uncertain")) > 0) {
            fields.put("type", "uncertain_choice");
        } else {
            fields.put("type", count(fields.get("answer")) == 1 ? "single_choice" : "choice");
        }

        fields.put("metas", FastHashMap.build(1).add("choices", fields.get("choices")).toMap());
        return this.commonFilter(fields, mode);
    }

    private Object[] convertQuestionAnswerToArray(Map<String, Object> question) {
        List<?> questionAnswerList = (List<?>) (question.get("answer"));
        return questionAnswerList == null || questionAnswerList.size() < 1 ? new Object[0] :
                questionAnswerList.toArray();
    }

    @Override
    public Map<String, Object> judge(Map<String, Object> question, Object[] answer) {
        Object[] questionAnswers = convertQuestionAnswerToArray(question);
        if (arrayDiff(questionAnswers, answer).size() == 0 && arrayDiff(answer, questionAnswers).size() == 0) {
            return FastHashMap.build(1).add("status", "right").toMap();
        }

        if (arrayDiff(answer, questionAnswers).size() == 0) {
            int percentage = (int) (1.0 * answer.length / questionAnswers.length * 100);
            return FastHashMap.build(2).add("status", "partRight").add("percentage", percentage).toMap();
        }

        return FastHashMap.build(1).add("status", "wrong").toMap();
    }

}
