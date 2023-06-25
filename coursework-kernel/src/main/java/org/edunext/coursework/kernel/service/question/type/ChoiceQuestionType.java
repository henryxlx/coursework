package org.edunext.coursework.kernel.service.question.type;

import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.ValueParser;

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

    @Override
    public Map<String, Object> judge(Map<String, Object> question, Object[] answer) {
        if (count(arrayDiff(question.get("answer"), answer)) == 0 && count(arrayDiff(answer, question.get("answer"))) == 0) {
            return FastHashMap.build(1).add("status", "right").toMap();
        }

        if (count(arrayDiff(answer, question.get("answer"))) == 0) {
            int percentage = (int) (1.0 * count(answer) / count(question.get("answer")) * 100);
            return FastHashMap.build(2).add("status", "partRight").add("percentage", percentage).toMap();
        }

        return FastHashMap.build(1).add("status", "wrong").toMap();
    }

}
