package org.edunext.coursework.kernel.service.question.type;

/**
 * @author xulixin
 */
public class UncertainChoiceQuestionType extends ChoiceQuestionType {

    @Override
    public boolean hasMissScore() {
        return true;
    }
}
