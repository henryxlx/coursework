package org.edunext.coursework.kernel.service.question.type;

import com.jetwinner.util.FastHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FillQuestionType extends AbstractQuestionType {

    @Override
    public Map<String, Object> filter(Map<String, Object> fields, String mode) {
        fields = this.commonFilter(fields, mode);

//        preg_match_all("/\[\[(.+?)\]\]/", fields.get("stem"), answer, PREG_PATTERN_ORDER);
//        if (empty($answer[1])){
//            throw new RuntimeGoingException("该问题没有答案或答案格式不正确！");
//        }
        Object[] answer = new Object[0];

        List<Object> rightAnswers = new ArrayList<>();
        for (int i = 0, len = answer != null ? answer.length : 0; i < len; i++) {
            String value = String.valueOf(answer[i]);
            String[] arr = value.split("|");
            if (arr != null && arr.length > 0) {
                for (String v : arr) {
                    rightAnswers.add(v);
                }
            }
        }
        if (rightAnswers.size() > 0) {
            fields.put("answer", rightAnswers.toArray());
        }

        return fields;
    }

    @Override
    public Map<String, Object> judge(Map<String, Object> question, Object[] answer) {
        Object[] questionAnswers = arrayValues(question.get("answer"));

        if (count(answer) != count(questionAnswers)) {
            return FastHashMap.build(1).add("status", "wrong").toMap();
        }

        int rightCount = 0;
        for (Object rightAnswer : questionAnswers) {
            List<Object> expectAnswer = new ArrayList<>();
//            foreach ($rightAnswer as $key => $value) {
//                $value = trim($value);
//                $value = preg_replace("/([\x20\s\t]){2,}/", " ", $value);
//                expectAnswer.add(value);
//            }

//            $actualAnswer = trim($answer[$index]);
//            $actualAnswer = preg_replace("/([\x20\s\t]){2,}/", " ", $actualAnswer);
//            if (in_array(actualAnswer, expectAnswer)) {
//                rightCount++;
//            }
        }

        if (rightCount == 0) {
            return FastHashMap.build(1).add("status", "wrong").toMap();
        } else if (rightCount < count(questionAnswers)) {
            int percentage = (int) (1.0 * rightCount / count(questionAnswers) * 100);
            return FastHashMap.build(1).add("status", "partRight").add("percentage", percentage).toMap();
        } else {
            return FastHashMap.build(1).add("status", "right").toMap();
        }
    }
}
