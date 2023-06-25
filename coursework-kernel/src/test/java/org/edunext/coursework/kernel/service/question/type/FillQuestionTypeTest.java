package org.edunext.coursework.kernel.service.question.type;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class FillQuestionTypeTest {

    @Test
    public void testFillQuestionAnswer() {
        String s = "《鲁滨逊漂流记》的作者是英国作家[[丹尼尔迪福]]。他在一座无人荒岛上生活多年后，收得一土人为奴，取名[[星期五]]。";
        Matcher m = Pattern.compile("(?<=\\[\\[).+?(?=\\]\\])").matcher(s);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        assertEquals(2, list.size());
        assertEquals("丹尼尔迪福", list.get(0));
        assertEquals("星期五", list.get(1));
    }

}