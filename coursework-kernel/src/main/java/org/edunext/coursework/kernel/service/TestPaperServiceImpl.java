package org.edunext.coursework.kernel.service;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.*;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.ActionGraspException;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.dao.TestPaperDao;
import org.edunext.coursework.kernel.dao.TestPaperItemDao;
import org.edunext.coursework.kernel.service.question.type.AbstractQuestionType;
import org.edunext.coursework.kernel.service.question.type.QuestionTypeFactory;
import org.edunext.coursework.kernel.service.testpaper.TestPaperBuildResult;
import org.edunext.coursework.kernel.service.testpaper.TestPaperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author xulixin
 */
@Service
public class TestPaperServiceImpl implements TestPaperService {

    private static final Logger log = LoggerFactory.getLogger(TestPaperServiceImpl.class);

    private final TestPaperDao testPaperDao;
    private final TestPaperItemDao testPaperItemDao;
    private final QuestionService questionService;
    private final ApplicationContext applicationContext;

    public TestPaperServiceImpl(TestPaperDao testPaperDao, TestPaperItemDao testPaperItemDao,
                                QuestionService questionService,
                                ApplicationContext applicationContext) {

        this.testPaperDao = testPaperDao;
        this.testPaperItemDao = testPaperItemDao;
        this.questionService = questionService;
        this.applicationContext = applicationContext;
    }

    @Override
    public Map<String, Object> getTestpaper(Object id) {
        return this.testPaperDao.getTestpaper(id);
    }

    @Override
    public Integer searchTestpapersCount(Map<String, Object> conditions) {
        return this.testPaperDao.searchTestpapersCount(conditions);
    }

    @Override
    public List<Map<String, Object>> searchTestpapers(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        return this.testPaperDao.searchTestpapers(conditions, orderBy, start, limit);
    }

    @Override
    public Integer createTestpaper(Map<String, Object> fields) {
        Integer testpaperId = this.testPaperDao.addTestpaper(this.filterTestpaperFields(fields, "create"));
        this.buildTestpaper(testpaperId, fields);
        return testpaperId;
    }

    @Override
    public List<Map<String, Object>> buildTestpaper(Object testPaperId, Map<String, Object> options) {
        Map<String, Object> testpaper = this.testPaperDao.getTestpaper(testPaperId);
        if (MapUtil.isEmpty(testpaper)) {
            throw new RuntimeGoingException("Testpaper #" + testPaperId + " is not found.");
        }

        this.testPaperItemDao.deleteItemsByTestPaperId(testPaperId);

        TestPaperBuilder builder = null;
        try {
            builder = getTestPaperBuilder(testpaper.get("pattern"));
        } catch (ActionGraspException e) {
            log.error("Build testpaper error: " + e.getMessage());
            return new ArrayList<>(0);
        }
        TestPaperBuildResult result = builder.build(testpaper, options);
        if (!"ok".equals(result.getStatus())) {
            throw new RuntimeGoingException("Build testpaper #" + testPaperId + " items error.");
        }

        List<Map<String, Object>> items = new ArrayList<>();
        List<Object> types = new ArrayList<>();

        int totalScore = 0;
        int seq = 1;
        for (Map<String, Object> item : result.getItems()) {
            AbstractQuestionType questionType = QuestionTypeFactory.create(item.get("questionType"));

            item.put("seq", seq);
            if (!questionType.canHaveSubQuestion()) {
                seq++;
                totalScore += ValueParser.parseFloat(item.get("score"));
            }

            items.add(this.testPaperItemDao.addItem(item));
            if (ValueParser.parseInt(item.get("parentId")) == 0 && !ListUtil.contains(item.get("questionType"), types)) {
                types.add(item.get("questionType"));
            }
        }

        Map<String, Object> metas = testpaper.get("metas") == null ? new HashMap<>(2) :
                ArrayToolkit.toMap(testpaper.get("metas"));
        metas.put("question_type_seq", types);
        metas.put("missScore", options.get("missScores"));

        this.testPaperDao.updateTestpaper(testPaperId, FastHashMap.build(3)
                .add("itemCount", seq - 1)
                .add("score", totalScore)
                .add("metas", metas).toMap());

        return items;
    }

    @Override
    public Map<String, Map<String, Object>> previewTestpaper(Integer testId) {
        return null;
    }

    @Override
    public Map<String, Object> publishTestpaper(Object testpaperId) {
        Map<String, Object> testpaper = this.testPaperDao.getTestpaper(testpaperId);
        if (MapUtil.isEmpty(testpaper)) {
            throw new RuntimeGoingException("试卷不存在！");
        }
        if (!ArrayUtil.inArray(testpaper.get("status"), "closed", "draft")) {
            throw new RuntimeGoingException("试卷状态不合法!");
        }
        testpaper = FastHashMap.build(1).add("status", "open").toMap();
        this.testPaperDao.updateTestpaper(testpaperId, testpaper);
        return this.getTestpaper(testpaperId);
    }

    @Override
    public Map<String, Object> closeTestpaper(Integer testpaperId) {
        Map<String, Object> testpaper = this.testPaperDao.getTestpaper(testpaperId);
        if (MapUtil.isEmpty(testpaper)) {
            throw new RuntimeGoingException("试卷不存在！");
        }
        if (!ArrayUtil.inArray(testpaper.get("status"), "open")) {
            throw new RuntimeGoingException("试卷状态不合法!");
        }
        testpaper = FastHashMap.build(1).add("status", "closed").toMap();
        this.testPaperDao.updateTestpaper(testpaperId, testpaper);
        return this.getTestpaper(testpaperId);
    }

    @Override
    public Map<String, Object> findTestpaperResultByTestpaperIdAndUserIdAndActive(Integer testpaperId, AppUser user) {
        return null;
    }

    @Override
    public Integer findTestpaperResultsCountByUserId(Integer userId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findTestpaperResultsByUserId(Integer userId, Integer start, Integer limit) {
        return new ArrayList<>(0);
    }

    @Override
    public List<Map<String, Object>> findTestpapersByIds(Set<Object> ids) {
        return new ArrayList<>(0);
    }

    private TestPaperBuilder getTestPaperBuilder(Object pattern) throws ActionGraspException {
        String beanName = StringUtils.uncapitalize(pattern + "TestPaperBuilder");
        TestPaperBuilder paperBuilder = applicationContext.getBean(beanName, TestPaperBuilder.class);
        if (paperBuilder == null) {
            throw new ActionGraspException(pattern + "TestPaperBuilder not found!");
        }
        return paperBuilder;
    }

    @Override
    public void deleteTestpaper(Object id) {
        this.testPaperDao.deleteTestpaper(id);
        this.testPaperItemDao.deleteItemsByTestPaperId(id);
    }

    @Override
    public void updateTestpaperItems(Object testpaperId, List<Map<String, Object>> items) {
        Map<String, Object> testpaper = this.getTestpaper(testpaperId);
        if (MapUtil.isEmpty(testpaper)) {
            throw new RuntimeGoingException("试卷#" + testpaperId + "不存在！不能处理试卷中的题目！");
        }

        Map<String, Map<String, Object>> existItems = ArrayToolkit.index(this.getTestpaperItems(testpaperId), "questionId");

        Map<String, Map<String, Object>> questions = this.questionService.findQuestionsByIds(ArrayToolkit.column(items, "questionId"));
        if (items.size() != questions.size()) {
            throw new RuntimeGoingException("数据缺失");
        }

        List<Object> types = new ArrayList<>();
        int totalScore = 0;
        int seq = 1;

        Map<String, Map<String, Object>> mapForItems = ArrayToolkit.index(items, "questionId");
        mapForItems.forEach((questionId, item) -> {
            if ("material".equals(questions.get(questionId).get("type"))) {
                mapForItems.get(questionId).put("score", 0);
            }
        });

        mapForItems.forEach((questionId, item) -> {
            if (ValueParser.parseInt(questions.get(questionId).get("parentId")) > 0) {
                String keyForParentId = "" + questions.get(questionId).get("parentId");
                int parentScore = ValueParser.parseInt(mapForItems.get(keyForParentId).get("score"));
                int itemScore = ValueParser.parseInt(item.get("score"));
                mapForItems.get(keyForParentId).put("score", parentScore + itemScore);
            }
        });

        for (Map<String, Object> item : mapForItems.values()) {
            Map<String, Object> question = questions.get("" + item.get("questionId"));
            item.put("seq", seq);
            if (ValueParser.parseInt(question.get("subCount")) == 0) {
                seq++;
                totalScore += ValueParser.parseInt(item.get("score"));
            }

            if (existItems.get("" + item.get("questionId")) == null) {
                item.put("questionType", question.get("type"));
                item.put("parentId", question.get("parentId"));
                // @todo, wellming.

                Map<String, Object> mapForMetas = ArrayToolkit.toMap(testpaper.get("metas"));
                if (mapForMetas.containsKey("missScore")) {
                    Map<String, Object> mapForMissScore = ArrayToolkit.toMap(mapForMetas.get("missScore"));
                    if (mapForMissScore.containsKey(question.get("type"))) {
                        item.put("missScore", mapForMissScore.get(question.get("type")));
                    }
                } else {
                    item.put("missScore", 0);
                }

                item.put("testId", testpaperId);
                item = this.testPaperItemDao.addItem(item);
            } else {

                Map<String, Object> existItem = existItems.get("" + item.get("questionId"));

                if (ValueParser.parseInt(item.get("seq")) != ValueParser.parseInt(existItem.get("seq")) ||
                        ValueParser.parseInt(item.get("score")) != ValueParser.parseInt(existItem.get("score"))) {

                    existItem.put("seq", item.get("seq"));
                    existItem.put("score", item.get("score"));
                    item = this.testPaperItemDao.updateItem(existItem.get("id"), existItem);
                } else {
                    item = existItem;
                }
                existItems.remove("" + item.get("questionId"));
            }

            if (ValueParser.parseInt(item.get("parentId")) == 0 && !ListUtil.contains(item.get("questionType"), types)) {
                types.add(item.get("questionType"));
            }
        }

        for (Map<String, Object> existItem : existItems.values()) {
            this.testPaperItemDao.deleteItem(existItem.get("id"));
        }

        Map<String, Object> metas = testpaper.get("metas") == null ? new HashMap<>(1) :
                ArrayToolkit.toMap(testpaper.get("metas"));
        metas.put("question_type_seq", types);

        this.testPaperDao.updateTestpaper(testpaper.get("id"), FastHashMap.build(3)
                .add("itemCount", seq - 1)
                .add("score", totalScore)
                .add("metas", metas).toMap());
    }

    @Override
    public List<Map<String, Object>> getTestpaperItems(Object testpaperId) {
        return this.testPaperItemDao.findItemsByTestPaperId(testpaperId);
    }

    @Override
    public void updateTestpaper(Integer testpaperId, Map<String, Object> fields) {
        fields = this.filterTestpaperFields(fields, "update");
        this.testPaperDao.updateTestpaper(testpaperId, fields);
    }

    private Map<String, Object> filterTestpaperFields(Map<String, Object> fields) {
        return filterTestpaperFields(fields, "create");
    }

    private Map<String, Object> filterTestpaperFields(Map<String, Object> fields, String mode) {
        Map<String, Object> filterMap = new HashMap<>(12);
        Integer currentUserId = AppUser.getCurrentUser(fields).getId();
        filterMap.put("updatedUserId", currentUserId);
        filterMap.put("updatedTime", System.currentTimeMillis());
        if ("create".equals(mode)) {
            if (!ArrayToolkit.required(fields, "name", "pattern", "target")) {
                throw new RuntimeGoingException("缺少必要字段！");
            }
            filterMap.put("name", fields.get("name"));
            filterMap.put("target", fields.get("target"));
            filterMap.put("pattern", fields.get("pattern"));
            filterMap.put("description", EasyStringUtil.isBlank(fields.get("description")) ? "" : fields.get("description"));
            filterMap.put("limitedTime", fields.get("limitedTime") == null ? 0 : fields.get("limitedTime"));
            filterMap.put("metas", fields.get("metas") == null ? new HashMap<>(0) : fields.get("metas"));
            filterMap.put("status", "draft");
            filterMap.put("createdUserId", currentUserId);
            filterMap.put("createdTime", System.currentTimeMillis());
        } else {
            if (fields.containsKey("name")) {
                filterMap.put("name", EasyStringUtil.isBlank(fields.get("name")) ? "" : fields.get("name"));
            }

            if (fields.containsKey("description")) {
                filterMap.put("description", EasyStringUtil.isBlank(fields.get("description")) ? "" : fields.get("description"));
            }

            if (fields.containsKey("limitedTime")) {
                filterMap.put("limitedTime", ValueParser.parseInt(fields.get("limitedTime")));
            }

            if (fields.containsKey("passedScore")) {
                filterMap.put("passedScore", ValueParser.parseFloat(fields.get("passedScore")));
            }
        }

        return filterMap;
    }
}
