package org.edunext.coursework.freemarker.ext;

import com.jetwinner.webfast.freemarker.ext.BaseFreeMarkerSharedVariableRegister;
import freemarker.template.SimpleScalar;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author xulixin
 */
@Component
public class CourseWorkFreeMarkerSharedVariableRegister extends BaseFreeMarkerSharedVariableRegister {

    public CourseWorkFreeMarkerSharedVariableRegister() {
        this.sharedVariableMap = new HashMap<>(1);
        fillQuestionStemTextFilter();
    }

    private void fillQuestionStemTextFilter() {
        String filterName = "fill_question_stem_text";
        this.sharedVariableMap.put(filterName, args -> {
            validFilterArguments(args, 1, filterName);
            SimpleScalar scalar = (SimpleScalar) args.get(0);
            String stem = scalar.getAsString();
            if (stem != null && stem.indexOf("[[") >= 0 && stem.indexOf("]]") >= 0) {
                return stem.replaceAll("\\[\\[.+?\\]\\]", "____");
            }
            return stem;
        });
    }
}
