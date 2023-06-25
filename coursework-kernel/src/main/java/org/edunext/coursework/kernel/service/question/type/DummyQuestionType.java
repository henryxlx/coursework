package org.edunext.coursework.kernel.service.question.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xulixin
 */
public class DummyQuestionType extends AbstractQuestionType {

    @Override
    public Map<String, Object> judge(Map<String, Object> question, Object[] answer) {
        return new HashMap<>(0);
    }
}
