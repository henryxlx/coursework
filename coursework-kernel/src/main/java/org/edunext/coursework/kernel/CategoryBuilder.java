package org.edunext.coursework.kernel;

import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Component
public class CategoryBuilder {

    private final AppCategoryService categoryService;

    public CategoryBuilder(AppCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Map<String, String> buildChoices(String groupCode) {
        return buildChoices(groupCode, "　");
    }

    public Map<String, String> buildChoices(String groupCode, String indent) {
        if (indent == null) {
            indent = "";
        }
        Map<String, Object> group = categoryService.getGroupByCode(groupCode);
        if (group == null) {
            return new HashMap<>(0);
        }


        List<Map<String, Object>> categories = categoryService.getCategoryTree(group.get("id"));
        Map<String, String> choices = new HashMap<>(categories.size());

        for (Map<String, Object> category : categories) {
            choices.put(String.valueOf(category.get("id")),
                    repeatStr(indent, category.get("depth")) + category.get("name"));
        }
        return choices;
    }

    private String repeatStr(String indent, Object depth) {
        int count = ValueParser.parseInt(depth) - 1;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < count; i++) {
            buf.append(indent);
        }
        return buf.toString();
    }

}
