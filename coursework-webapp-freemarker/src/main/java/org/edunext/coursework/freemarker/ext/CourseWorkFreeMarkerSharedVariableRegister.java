package org.edunext.coursework.freemarker.ext;

import com.jetwinner.webfast.freemarker.ext.BaseFreeMarkerSharedVariableRegister;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xulixin
 */
@Component
public class CourseWorkFreeMarkerSharedVariableRegister extends BaseFreeMarkerSharedVariableRegister {

    public CourseWorkFreeMarkerSharedVariableRegister() {
        this.sharedVariableMap = new HashMap<>(1);
        fillQuestionStemTextFilter();
        fillQuestionStemHtmlFilter();
        charFilter();
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

    private void fillQuestionStemHtmlFilter() {
        String filterName = "fill_question_stem_html";
        this.sharedVariableMap.put(filterName, args -> {
            validFilterArguments(args, 1, filterName);
            SimpleScalar scalar = (SimpleScalar) args.get(0);
            String stem = scalar.getAsString();
            if (stem != null && stem.indexOf("[[") >= 0 && stem.indexOf("]]") >= 0) {
                Matcher m = Pattern.compile("(?<=\\[\\[).+?(?=\\]\\])").matcher(stem);
                List<String> list = new ArrayList<>();
                while (m.find()) {
                    list.add(m.group());
                }
                for (int i = 0; i < list.size(); i++) {
                    String target = "[[" + list.get(i) + "]]";
                    stem = stem.replace(target, "<span class='question-stem-fill-blank'>(" + i + ")</span>");
                }
            }
            return stem;
        });
    }

    private void charFilter() {
        String filterName = "chr";
        this.sharedVariableMap.put(filterName, args -> {
            validFilterArguments(args, 1, filterName);
            SimpleNumber number = (SimpleNumber) args.get(0);
            int asciiCode = number.getAsNumber().intValue();
            if (asciiCode >= 0 && asciiCode < 256) {
                char ch = (char) asciiCode;
                return "" + ch;
            }
            return "" + asciiCode;
        });
    }
}
