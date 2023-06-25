package org.edunext.coursework.kernel.service.question.type;

import com.jetwinner.util.FastHashMap;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FillQuestionType extends AbstractQuestionType {

    @Override
    public Map<String, Object> filter(Map<String, Object> fields, String mode) {
        fields = this.commonFilter(fields, mode);

        List<String> answers = new ArrayList<>();
        Matcher m = Pattern.compile("(?<=\\[\\[).+?(?=\\]\\])").matcher(String.valueOf(fields.get("stem")));
        while (m.find()) {
            answers.add(m.group());
        }
        if (answers.size() == 0) {
            throw new RuntimeGoingException("该问题没有答案或答案格式不正确！");
        }

        List<Object> rightAnswers = new ArrayList<>();
        for (String value : answers) {
            String[] arr = value.split("\\|");
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
