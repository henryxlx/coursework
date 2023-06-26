package org.edunext.coursework.kernel.service.question.type;

/**
 * @author xulixin
 */
public class SingleChoiceQuestionType extends ChoiceQuestionType {

    @Override
    public boolean hasMissScore() {
        return false;
    }
}
