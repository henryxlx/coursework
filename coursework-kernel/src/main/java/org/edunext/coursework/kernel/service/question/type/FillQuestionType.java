package org.edunext.coursework.kernel.service.question.type;

import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xulixin
 */
public class FillQuestionType extends AbstractQuestionType {

    private static final Pattern FILL_BLANK_PATTERN = Pattern.compile("(?<=\\[\\[).+?(?=\\]\\])");

    @Override
    public Map<String, Object> filter(Map<String, Object> fields, String mode) {
        fields = this.commonFilter(fields, mode);

        List<String> answers = new ArrayList<>();
        Matcher m = FILL_BLANK_PATTERN.matcher(String.valueOf(fields.get("stem")));
        while (m.find()) {
            answers.add(m.group());
        }
        if (answers.size() == 0) {
            throw new RuntimeGoingException("该问题没有答案或答案格式不正确！");
        }

        List<Object> rightAnswers = new ArrayList<>(answers.size());
        for (String value : answers) {
            String[] arr = value.split("\\|");
            for (int i = 0, len = arr != null ? arr.length : 0; i < len; i++) {
                if (arr[i] != null) {
                    arr[i] = arr[i].trim();
                }
            }
            rightAnswers.add(arr);
        }
        if (rightAnswers.size() > 0) {
            fields.put("answer", rightAnswers);
        }

        return fields;
    }

    @Override
    public Map<String, Object> judge(Map<String, Object> question, Object[] answer) {
        List<List<?>> questionAnswers = (List) (question.get("answer"));

        int questionAnswerCount = questionAnswers.size();
        if (answer.length != questionAnswerCount) {
            return FastHashMap.build(1).add("status", "wrong").toMap();
        }

        int rightCount = 0;
        int index = 0;
        for (List<?> rightAnswer : questionAnswers) {
            List<String> expectAnswer = new ArrayList<>(rightAnswer.size());
            for (Object obj : rightAnswer) {
                String value = String.valueOf(obj).trim();
                // 消除空格，制表符、回车和换行(x20\s\t)
                value = value.replaceAll("\\s*|\t|\r|\n|　*", "");
                expectAnswer.add(value);
            }

            String actualAnswer = String.valueOf(answer[index]);
            actualAnswer = actualAnswer.trim();
            // 消除空格，制表符、回车和换行(\\s*|\t|\r|\n),还有中文空格 preg_replace("/([\x20\s\t]){2,}/", " ", $actualAnswer);
            actualAnswer = actualAnswer.replaceAll("\\s*|\t|\r|\n|　*", "");
            if (ArrayUtil.inArray(actualAnswer, expectAnswer.toArray(new String[expectAnswer.size()]))) {
                rightCount++;
            }
        }

        if (rightCount == 0) {
            return FastHashMap.build(1).add("status", "wrong").toMap();
        } else if (rightCount < questionAnswerCount) {
            int percentage = (int) (1.0 * rightCount / questionAnswerCount * 100);
            return FastHashMap.build(1).add("status", "partRight").add("percentage", percentage).toMap();
        } else {
            return FastHashMap.build(1).add("status", "right").toMap();
        }
    }
}
