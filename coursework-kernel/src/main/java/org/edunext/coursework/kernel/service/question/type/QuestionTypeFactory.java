package org.edunext.coursework.kernel.service.question.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xulixin
 */
public final class QuestionTypeFactory {

    private static final QuestionTypeFactory instance = new QuestionTypeFactory();

    public static AbstractQuestionType create(Object type) {
        AbstractQuestionType qtHandler = instance.cachedMap.get(String.valueOf(type));
        return qtHandler != null ? qtHandler : new DummyQuestionType();
    }

    private final Map<String, AbstractQuestionType> cachedMap;

    private QuestionTypeFactory() {
        cachedMap = new HashMap<>(7);
        cachedMap.put("choice", new ChoiceQuestionType());
        cachedMap.put("determine", new DetermineQuestionType());
        cachedMap.put("essay", new EssayQuestionType());
        cachedMap.put("fill", new FillQuestionType());
        cachedMap.put("material", new MaterialQuestionType());
        cachedMap.put("single_choice", new SingleChoiceQuestionType());
        cachedMap.put("uncertain_choice", new UncertainChoiceQuestionType());
    }
}
