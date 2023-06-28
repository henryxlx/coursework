package org.edunext.coursework.kernel.service.testpaper;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.*;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.service.QuestionService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("questionTypeTestPaperBuilder")
public class QuestionTypeTestPaperBuilder implements TestPaperBuilder {

    private final QuestionService questionService;

    public QuestionTypeTestPaperBuilder(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public TestPaperBuildResult build(Map<String, Object> testpaper, Map<String, Object> options) {
        List<Map<String, Object>> questions = this.getQuestions(options);
        // shuffle($questions);
        Map<String, List<Map<String, Object>>> typedQuestions = ArrayToolkit.group(questions, "type");

        TestPaperBuildResult canBuildResult = this.canBuildWithQuestions(options, typedQuestions);
        if ("no".equals(canBuildResult.getStatus())) {
            return canBuildResult.setStatus("error");
        }

        List<Map<String, Object>> items = new ArrayList<>();
        Set<String> keyForCounts = options.keySet().stream().filter(e -> e.startsWith("counts")).collect(Collectors.toSet());
        for (String keyForCount : keyForCounts) {
            String type = keyForCount.replace("counts[", "").replace("]", "");
            int needCount = ValueParser.parseInt(options.get(keyForCount));
            if (needCount == 0) {
                continue;
            }

            int missScore = ValueParser.parseInt(options.get("missScores[" + type + "]"));

            List<Map<String, Object>> itemsOfType;
            if ("difficulty".equals(options.get("mode"))) {
                Map<String, List<Map<String, Object>>> difficultiedQuestions = ArrayToolkit.group(typedQuestions.get(type), "difficulty");

                // 按难度百分比选取Question
                List<Map<String, Object>> selectedQuestions = this.selectQuestionsWithDifficultlyPercentage(difficultiedQuestions, needCount, options);

                // 选择的Question不足的话，补足
                selectedQuestions = this.fillQuestionsToNeedCount(selectedQuestions, typedQuestions.get(type), needCount);

                itemsOfType = this.convertQuestionsToItems(testpaper, selectedQuestions, needCount, options);
            } else {
                itemsOfType = this.convertQuestionsToItems(testpaper, typedQuestions.get(type), needCount, options);
            }
            items.addAll(itemsOfType);
        }

        return new TestPaperBuildResult().setStatus("ok").setItems(items);
    }

    @Override
    public TestPaperBuildResult canBuild(Map<String, Object> options) {
        List<Map<String, Object>> questions = this.getQuestions(options);
        Map<String, List<Map<String, Object>>> typedQuestions = ArrayToolkit.group(questions, "type");
        return this.canBuildWithQuestions(options, typedQuestions);
    }

    private List<Map<String, Object>> fillQuestionsToNeedCount(List<Map<String, Object>> selectedQuestions,
                                                               List<Map<String, Object>> allQuestions, int needCount) {

        Map<String, Map<String, Object>> indexedQuestions = ArrayToolkit.index(allQuestions, "id");
        selectedQuestions.forEach(question -> indexedQuestions.remove(String.valueOf(question.get("id"))));

        int stillNeedCount = 0;
        if (selectedQuestions.size() < needCount) {
            stillNeedCount = needCount - selectedQuestions.size();
        }

        if (stillNeedCount > 0) {
            List<Map<String, Object>> questions = TempCollectionUtil.arraySlice(indexedQuestions.values(), 0, stillNeedCount);
            selectedQuestions.addAll(questions);
        }


        return selectedQuestions;
    }

    private List<Map<String, Object>> selectQuestionsWithDifficultlyPercentage(Map<String, List<Map<String, Object>>> difficultiedQuestions,
                                                                               int needCount, Map<String, Object> options) {

        List<Map<String, Object>> selectedQuestions = new ArrayList<>();
        Set<String> keyForPercentages = options.keySet().stream().filter(e -> e.startsWith("percentages")).collect(Collectors.toSet());
        for (String keyForPercentage : keyForPercentages) {
            String difficulty = keyForPercentage.replace("percentages[", "").replace("]", "");
            int percentage = ValueParser.parseInt(keyForPercentage);
            int subNeedCount = (int) (1.0 * needCount * percentage / 100);
            if (subNeedCount == 0) {
                continue;
            }

            if (difficultiedQuestions.get(difficulty) != null) {
                List<Map<String, Object>> questions = TempCollectionUtil.arraySlice(difficultiedQuestions.get(difficulty), 0, subNeedCount);
                selectedQuestions.addAll(questions);
            }
        }

        return selectedQuestions;
    }

    private TestPaperBuildResult canBuildWithQuestions(Map<String, Object> options, Map<String, List<Map<String, Object>>> questions) {
        Map<String, Object> missing = new HashMap<>(8);

        Set<String> keyForCounts = options.keySet().stream().filter(e -> e.startsWith("counts")).collect(Collectors.toSet());
        for (String keyForCount : keyForCounts) {
            String type = keyForCount.replace("counts[", "").replace("]", "");
            int needCount = ValueParser.parseInt(options.get(keyForCount));
            if (needCount == 0) {
                continue;
            }

            if (questions.get(type) == null) {
                missing.put(type, needCount);
                continue;
            }

            if ("material".equals(type)) {
                int validatedMaterialQuestionNum = 0;
                List<Map<String, Object>> materialQuestions = questions.get("material");
                if (materialQuestions != null) {
                    for (Map<String, Object> materialQuestion : materialQuestions) {
                        if (ValueParser.parseInt(materialQuestion.get("subCount")) > 0) {
                            validatedMaterialQuestionNum += 1;
                        }
                    }
                }
                if (validatedMaterialQuestionNum < needCount) {
                    missing.put("material", needCount - validatedMaterialQuestionNum);
                }
                continue;
            }
            int countForQuestionByType = questions.get(type) == null ? 0 : questions.get(type).size();
            if (countForQuestionByType < needCount) {
                missing.put(type, needCount - countForQuestionByType);
            }
        }

        if (MapUtil.isEmpty(missing)) {
            return new TestPaperBuildResult().setStatus("yes");
        }

        return new TestPaperBuildResult().setStatus("no").setMissing(missing);
    }

    private List<Map<String, Object>> getQuestions(Map<String, Object> options) {
        Map<String, Object> conditions = new HashMap<>(2);

        if (options.get("ranges") != null && ArrayUtil.getLength(options.get("ranges")) > 0) {
            conditions.put("targets", options.get("ranges"));
        } else {
            conditions.put("targetPrefix", options.get("target"));
        }

        conditions.put("parentId", 0);

        int total = this.questionService.searchQuestionsCount(conditions);

        return this.questionService.searchQuestions(conditions, OrderBy.build(1).addDesc("createdTime"), 0, total);
    }

    private List<Map<String, Object>> convertQuestionsToItems(Map<String, Object> testpaper,
                                                              List<Map<String, Object>> questions,
                                                              int count,
                                                              Map<String, Object> options) {

        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Map<String, Object> question = questions.get(i);
            int score = ValueParser.parseInt(options.get("scores[" + question.get("type") + "]"));
            int missScore = ValueParser.parseInt(options.get("missScores[" + question.get("type") + "]"));
            items.add(this.makeItem(testpaper, question, score, missScore));
            int questionSubCount = ValueParser.parseInt(question.get("subCount"));
            if (questionSubCount > 0) {
                List<Map<String, Object>> subQuestions = this.questionService.findQuestionsByParentId(ValueParser.toInteger(question.get("id")));
                for (Map<String, Object> subQuestion : subQuestions) {
                    missScore = ValueParser.parseInt(options.get("missScores[" + subQuestion.get("type") + "]"));
                    items.add(this.makeItem(testpaper, subQuestion, score, missScore));
                }
            }
        }
        return items;
    }

    private Map<String, Object> makeItem(Map<String, Object> testpaper, Map<String, Object> question,
                                         Object score, Object missScore) {

        return FastHashMap.build(6)
                .add("testId", testpaper.get("id"))
                .add("questionId", question.get("id"))
                .add("questionType", question.get("type"))
                .add("parentId", question.get("parentId"))
                .add("score", score).add("missScore", missScore).toMap();
    }
}
