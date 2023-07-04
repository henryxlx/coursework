package org.edunext.coursework.kernel.service;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.*;
import com.jetwinner.webfast.event.ServiceEvent;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.ActionGraspException;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppUserService;
import org.edunext.coursework.kernel.dao.TestPaperDao;
import org.edunext.coursework.kernel.dao.TestPaperItemDao;
import org.edunext.coursework.kernel.dao.TestPaperItemResultDao;
import org.edunext.coursework.kernel.dao.TestPaperResultDao;
import org.edunext.coursework.kernel.service.question.type.AbstractQuestionType;
import org.edunext.coursework.kernel.service.question.type.QuestionTypeFactory;
import org.edunext.coursework.kernel.service.testpaper.TestPaperBuildResult;
import org.edunext.coursework.kernel.service.testpaper.TestPaperBuilder;
import org.edunext.coursework.kernel.service.testpaper.TestPaperExamResult;
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
    private final TestPaperResultDao testPaperResultDao;
    private final TestPaperItemResultDao testPaperItemResultDao;
    private final CourseService courseService;
    private final QuestionService questionService;
    private final UserAccessControlService userAccessControlService;
    private final AppUserService userService;
    private final ApplicationContext applicationContext;

    public TestPaperServiceImpl(TestPaperDao testPaperDao,
                                TestPaperItemDao testPaperItemDao,
                                TestPaperResultDao testPaperResultDao,
                                TestPaperItemResultDao testPaperItemResultDao,
                                CourseService courseService,
                                QuestionService questionService,
                                UserAccessControlService userAccessControlService,
                                AppUserService userService, ApplicationContext applicationContext) {

        this.testPaperDao = testPaperDao;
        this.testPaperItemDao = testPaperItemDao;
        this.testPaperResultDao = testPaperResultDao;
        this.testPaperItemResultDao = testPaperItemResultDao;
        this.courseService = courseService;
        this.questionService = questionService;
        this.userAccessControlService = userAccessControlService;
        this.userService = userService;
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
    public Map<String, Map<String, Object>> previewTestpaper(Integer testpaperId) {
        List<Map<String, Object>> list = this.getTestpaperItems(testpaperId);
        Map<String, Map<String, Object>> items = ArrayToolkit.index(list, "questionId");
        Map<String, Map<String, Object>> questions = this.questionService.findQuestionsByIds(ArrayToolkit.column(list, "questionId"));

        questions = this.completeQuestion(items, questions);

        Map<String, Map<String, Object>> formatItems = new HashMap<>();
        Iterator<Map.Entry<String, Map<String, Object>>> it = items.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Map<String, Object>> entry = it.next();
            String questionId = entry.getKey();
            Map<String, Object> item = entry.getValue();
            items.get(questionId).put("question", questions.get(questionId));

            if (ValueParser.parseInt(item.get("parentId")) != 0) {
                Map<String, Object> mapParentQuestion = items.get("" + item.get("parentId"));
                Map<String, Object> mapItems;
                if (!mapParentQuestion.containsKey("items")) {
                    mapItems = new HashMap<>();
                    mapParentQuestion.put("items", mapItems);
                } else {
                    mapItems = ArrayToolkit.toMap(mapParentQuestion.get("item"));
                }
                // $items[$item['parentId']]['items'][$questionId] = $items[$questionId];
                mapItems.put(questionId, item);

                // formatItems['material'][$item['parentId']]['items'][$item['seq']] = $items[$questionId];
                putFormatItemsBySeq(formatItems, item);
                it.remove(); // items.remove(questionId);
            } else {
                // formatItems.get(item.get("questionType")).put("" + item.get("questionId"), items.get(questionId));
                Map<String, Object> mapForItem;
                if (formatItems.containsKey(item.get("questionType"))) {
                    mapForItem = formatItems.get("" + item.get("questionType"));
                } else {
                    mapForItem = new HashMap<>();
                    formatItems.put("" + item.get("questionType"), mapForItem);
                }
                mapForItem.put("" + item.get("questionId"), item);
            }

        }

        // ksort($formatItems);
        return formatItems;
    }

    private void putFormatItemsBySeq(Map<String, Map<String, Object>> formatItems, Map<String, Object> item) {
        Map<String, Object> mapForMaterial = formatItems.get("material");
        if (mapForMaterial == null) {
            mapForMaterial = new HashMap<>();
            formatItems.put("material", mapForMaterial);
        }
        Map<String, Object> mapFormatItems;
        String parentId = String.valueOf(item.get("parentId"));
        if (mapForMaterial.get(parentId) == null) {
            mapFormatItems = new HashMap<>();
            mapForMaterial.put(parentId, mapForMaterial);
        } else {
            mapFormatItems = ArrayToolkit.toMap(mapForMaterial.get(parentId));
        }
        Map<String, Object> mapItemSeq;
        if (mapFormatItems.get("items") == null) {
            mapItemSeq = new HashMap<>();
            mapFormatItems.put("items", mapFormatItems);
        } else {
            mapItemSeq = ArrayToolkit.toMap(mapFormatItems.get("items"));
        }
        mapItemSeq.put("" + item.get("seq"), item);
    }

    private Map<String, Map<String, Object>> completeQuestion(Map<String, Map<String, Object>> items,
                                                              Map<String, Map<String, Object>> questions) {

        String[] arrQuestionIds = questions.keySet().toArray(new String[questions.keySet().size()]);
        items.entrySet().forEach(e -> {
            Map<String, Object> item = e.getValue();
            if (!ArrayUtil.inArray("" + item.get("questionId"), arrQuestionIds)) {
                questions.put(String.valueOf(item.get("questionId")),
                        FastHashMap.build(4)
                                .add("isDeleted", Boolean.TRUE)
                                .add("stem", "此题已删除")
                                .add("score", 0)
                                .add("answer", "").toMap());
            }
        });
        return questions;
    }

    @Override
    public Map<String, Object> findTestpaperResultByTestpaperIdAndUserIdAndActive(Integer testpaperId, Integer userId) {
        return this.testPaperResultDao.findTestpaperResultByTestpaperIdAndUserIdAndActive(testpaperId, userId);
    }

    @Override
    public Integer findTestpaperResultsCountByUserId(Integer userId) {
        return this.testPaperResultDao.findTestpaperResultsCountByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> findTestpaperResultsByUserId(Integer userId, Integer start, Integer limit) {
        return this.testPaperResultDao.findTestpaperResultsByUserId(userId, start, limit);
    }

    @Override
    public List<Map<String, Object>> findTestpapersByIds(Set<Object> ids) {
        return this.testPaperDao.findTestpapersByIds(ids);
    }

    @Override
    public Map<String, Object> startTestpaper(Integer testId, Map<String, Object> target) {
        Map<String, Object> testpaper = this.testPaperDao.getTestpaper(testId);

        Map<String, Object> testpaperResult = FastHashMap.build(8)
                .add("paperName", testpaper.get("name"))
                .add("testId", testId)
                .add("userId", AppUser.getCurrentUser(target).getId())
                .add("limitedTime", testpaper.get("limitedTime"))
                .add("beginTime", System.currentTimeMillis())
                .add("status", "doing")
                .add("usedTime", 0)
                .add("target", EasyStringUtil.isBlank(target.get("type")) ? "" :
                        testpaper.get("target") + "/" + target.get("type") + "-" + target.get("id")).toMap();

        return this.testPaperResultDao.addTestpaperResult(testpaperResult);
    }

    @Override
    public Map<String, Object> getTestpaperResult(Integer id) {
        return this.testPaperResultDao.getTestpaperResult(id);
    }

    @Override
    public TestPaperExamResult showTestpaper(Integer testpaperResultId, boolean isAccuracy) {
        List<Map<String, Object>> list = this.testPaperItemResultDao.findTestResultsByTestpaperResultId(testpaperResultId);
        Map<String, Map<String, Object>> itemResults = ArrayToolkit.index(list, "questionId");

        Map<String, Object> testpaperResult = this.testPaperResultDao.getTestpaperResult(testpaperResultId);

        list = this.getTestpaperItems(testpaperResult.get("testId"));
        Map<String, Map<String, Object>> items = ArrayToolkit.index(list, "questionId");

        Map<String, Map<String, Object>> questions = this.questionService.findQuestionsByIds(ArrayToolkit.column(list, "questionId"));

        questions = this.completeQuestion(items, questions);

        Map<String, Map<String, Object>> formatItems = new HashMap<>();
        Iterator<Map.Entry<String, Map<String, Object>>> it = items.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Map<String, Object>> entry = it.next();
            String questionId = entry.getKey();
            Map<String, Object> item = entry.getValue();

            if (itemResults.containsKey(questionId)) {
                questions.get(questionId).put("testResult", itemResults.get(questionId));
            }

            items.get(questionId).put("question", questions.get(questionId));

            if (ValueParser.parseInt(item.get("parentId")) != 0) {
                Map<String, Object> mapParentQuestion = items.get("" + item.get("parentId"));
                Map<String, Object> mapItems;
                if (!mapParentQuestion.containsKey("items")) {
                    mapItems = new HashMap<>();
                    mapParentQuestion.put("items", mapItems);
                } else {
                    mapItems = ArrayToolkit.toMap(mapParentQuestion.get("item"));
                }
                // $items[$item['parentId']]['items'][$questionId] = $items[$questionId];
                mapItems.put(questionId, item);

                // formatItems['material'][$item['parentId']]['items'][$item['seq']] = $items[$questionId];
                putFormatItemsBySeq(formatItems, item);
                it.remove(); // items.remove(questionId);
            } else {
                Map<String, Object> mapForItem;
                if (formatItems.containsKey(item.get("questionType"))) {
                    mapForItem = formatItems.get("" + item.get("questionType"));
                } else {
                    mapForItem = new HashMap<>();
                    formatItems.put("" + item.get("questionType"), mapForItem);
                }
                mapForItem.put("" + item.get("questionId"), item);
            }

        }

        TestPaperExamResult examResult = new TestPaperExamResult();
        if (isAccuracy) {
            examResult.setAccuracy(this.makeAccuracy(items));
        }

        // ksort($formatItems);
        examResult.setFormatItems(formatItems);
        return examResult;
    }

    private Map<String, Object> newAccuracyResult() {
        Map<String, Object> accuracyResult = new HashMap<>(7);
        accuracyResult.put("right", 0);
        accuracyResult.put("partRight", 0);
        accuracyResult.put("wrong", 0);
        accuracyResult.put("noAnswer", 0);
        accuracyResult.put("all", 0);
        accuracyResult.put("score", 0);
        accuracyResult.put("totalScore", 0);
        return accuracyResult;
    }

    private Map<String, Map<String, Object>> makeAccuracy(Map<String, Map<String, Object>> items) {
        Map<String, Map<String, Object>> accuracy = new HashMap<>(7);
        accuracy.put("single_choice", newAccuracyResult());
        accuracy.put("choice", newAccuracyResult());
        accuracy.put("uncertain_choice", newAccuracyResult());
        accuracy.put("determine", newAccuracyResult());
        accuracy.put("fill", newAccuracyResult());
        accuracy.put("essay", newAccuracyResult());
        accuracy.put("material", newAccuracyResult());

        for (Map<String, Object> item : items.values()) {
            if ("material".equals(item.get("questionType"))) {

                if (item.get("items") == null) {
                    continue;
                }

                Map<String, Object> mapForItem = ArrayToolkit.toMap(item.get("items"));

                for (Object objEntry : mapForItem.values()) {
                    Map<String, Object> mapForValue = ArrayToolkit.toMap(objEntry);
                    if (MapUtil.isEmpty(mapForValue)) {
                        continue;
                    }
                    if ("essay".equals(mapForValue.get("questionType"))) {
                        accuracy.get("material").put("hasEssay", Boolean.TRUE); // hasEssay = true;
                    }
                    Map<String, Object> mapQuestion = ArrayToolkit.toMap(item.get("question"));
                    Map<String, Object> mapForTestResult = ArrayToolkit.toMap(mapQuestion.get("testResult"));
                    if (MapUtil.isEmpty(mapForTestResult)) {
                        continue;
                    }

                    float score = ValueParser.parseFloat(accuracy.get("material").get("score"));
                    accuracy.get("material").put("score", score + ValueParser.parseFloat(mapForTestResult.get("score")));
                    score = ValueParser.parseFloat(accuracy.get("material").get("totalScore"));
                    accuracy.get("material").put("totalScore", score + ValueParser.parseFloat(mapForValue.get("score")));

                    int qnums = ValueParser.parseInt(accuracy.get("material").get("all"));
                    accuracy.get("material").put("all", ++qnums);
                    if ("right".equals(mapForTestResult.get("status"))) {
                        qnums = ValueParser.parseInt(accuracy.get("material").get("right"));
                        accuracy.get("material").put("right", ++qnums);
                    }
                    if ("partRight".equals(mapForTestResult.get("status"))) {
                        qnums = ValueParser.parseInt(accuracy.get("material").get("partRight"));
                        accuracy.get("material").put("partRight", ++qnums);
                    }
                    if ("wrong".equals(mapForTestResult.get("status"))) {
                        qnums = ValueParser.parseInt(accuracy.get("material").get("wrong"));
                        accuracy.get("material").put("wrong", ++qnums);
                    }
                    if ("noAnswer".equals(mapForTestResult.get("status"))) {
                        qnums = ValueParser.parseInt(accuracy.get("material").get("noAnswer"));
                        accuracy.get("material").put("noAnswer", ++qnums);
                    }
                }
            } else {

                Map<String, Object> mapQuestion = ArrayToolkit.toMap(item.get("question"));
                Map<String, Object> mapForTestResult = ArrayToolkit.toMap(mapQuestion.get("testResult"));
                if (MapUtil.isEmpty(mapForTestResult)) {
                    continue;
                }

                float score = ValueParser.parseFloat(accuracy.get(item.get("questionType")).get("score"));
                accuracy.get(item.get("questionType")).put("score", score + ValueParser.parseFloat(mapForTestResult.get("score")));
                score = ValueParser.parseFloat(accuracy.get(item.get("questionType")).get("totalScore"));
                accuracy.get(item.get("questionType")).put("totalScore", score + ValueParser.parseFloat(item.get("score")));

                int qnums = ValueParser.parseInt(accuracy.get(item.get("questionType")).get("all"));
                accuracy.get(item.get("questionType")).put("all", ++qnums);
                if ("right".equals(mapForTestResult.get("status"))) {
                    qnums = ValueParser.parseInt(accuracy.get(item.get("questionType")).get("right"));
                    accuracy.get(item.get("questionType")).put("right", ++qnums);
                }
                if ("partRight".equals(mapForTestResult.get("status"))) {
                    qnums = ValueParser.parseInt(accuracy.get(item.get("questionType")).get("partRight"));
                    accuracy.get(item.get("questionType")).put("partRight", ++qnums);
                }
                if ("wrong".equals(mapForTestResult.get("status"))) {
                    qnums = ValueParser.parseInt(accuracy.get(item.get("questionType")).get("wrong"));
                    accuracy.get(item.get("questionType")).put("wrong", ++qnums);
                }
                if ("noAnswer".equals(mapForTestResult.get("status"))) {
                    qnums = ValueParser.parseInt(accuracy.get(item.get("questionType")).get("noAnswer"));
                    accuracy.get(item.get("questionType")).put("noAnswer", ++qnums);
                }

            }
        }

        return accuracy;
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

    @Override
    public List<Map<String, Object>> findAllTestpapersByTarget(Integer id) {
        String target = "course-" + id;
        return this.testPaperDao.findTestpaperByTargets(target);
    }

    @Override
    public Integer findTestpaperResultCountByStatusAndTestIds(Set<Object> testpaperIds, String status) {
        return this.testPaperResultDao.findTestpaperResultCountByStatusAndTestIds(testpaperIds, status);
    }

    @Override
    public List<Map<String, Object>> findTestpaperResultsByStatusAndTestIds(Set<Object> testpaperIds, String status,
                                                                            Integer start, Integer limit) {

        return this.testPaperResultDao.findTestpaperResultsByStatusAndTestIds(testpaperIds, status, start, limit);
    }

    @Override
    public Integer canTeacherCheck(Object testpaperId, AppUser currentUser) {
        Map<String, Object> paper = this.testPaperDao.getTestpaper(testpaperId);
        if (MapUtil.isEmpty(paper)) {
            throw new RuntimeGoingException("试卷不存在");
        }

        if (userAccessControlService.isAdmin()) {
            return currentUser.getId();
        }

        String[] target = EasyStringUtil.explode("-", paper.get("target"));
        target = target == null || target.length < 2 ? new String[1] : target;

        if ("course".equals(target[0])) {
            String[] targetId = EasyStringUtil.explode("/", target[1]);
            targetId = targetId == null || targetId.length < 1 ? new String[]{"0"} : targetId;
            Map<String, Object> member = this.courseService.getCourseMember(ValueParser.toInteger(targetId[0]),
                    currentUser.getId());

            // @todo: 这个是有问题的。
            if ("teacher".equals(member.get("role"))) {
                return currentUser.getId();
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> submitTestpaperAnswer(Integer testpaperId, Map<String, Object> answers, AppUser user) {
        if (MapUtil.isEmpty(answers)) {
            return new ArrayList<>(0);
        }

        Map<String, Object> testpaperResult = this.testPaperResultDao.getTestpaperResult(testpaperId);

        if (ValueParser.parseInt(testpaperResult.get("userId")) != user.getId()) {
            throw new RuntimeGoingException("无权修改其他学员的试卷！");
        }

        if (ArrayUtil.inArray(testpaperResult.get("status"), "reviewing", "finished")) {
            throw new RuntimeGoingException("已经交卷的试卷不能更改答案!");
        }

        //已经有记录的
        List<Map<String, Object>> itemResults = this.filterTestAnswers(testpaperResult.get("id"), answers);
        Map<String, Map<String, Object>> itemIdsOld = ArrayToolkit.index(itemResults, "questionId");

        Map<String, Object> answersOld = ArrayToolkit.part(answers, itemIdsOld.keySet().stream().toArray(String[]::new));

        if (MapUtil.isNotEmpty(answersOld)) {
            this.testPaperItemResultDao.updateItemAnswers(testpaperResult.get("id"), answersOld);
        }
        //还没记录的
        Set<String> itemIdsNew = this.diff(answers.keySet(), itemIdsOld.keySet());

        Map<String, Object> answersNew = ArrayToolkit.part(answers, itemIdsNew.stream().toArray(String[]::new));

        if (MapUtil.isNotEmpty(answersNew)) {
            this.testPaperItemResultDao.addItemAnswers(testpaperResult.get("id"), answersNew,
                    testpaperResult.get("testId"), user.getId());
        }

        //测试数据
        return this.filterTestAnswers(testpaperResult.get("id"), answers);
    }

    private List<Map<String, Object>> filterTestAnswers(Object testpaperResultId, Map<String, Object> answers) {
        return this.testPaperItemResultDao.findTestResultsByItemIdAndTestId(answers.keySet(), testpaperResultId);
    }

    private Set<String> diff(Set<String> a, Set<String> b) {
        Set<String> dif = new HashSet<>();
        for (String s : b) {
            if (!(a.contains(s))) {
                dif.add(s);
            }
        }
        for (String s : a) {
            if (!(b.contains(s))) {
                dif.add(s);
            }
        }
        return dif;
    }

    @Override
    public void updateTestpaperResult(Integer testpaperId, Object usedTime) {
        Map<String, Object> testpaperResult = this.testPaperResultDao.getTestpaperResult(testpaperId);

        Map<String, Object> fields = new HashMap<>(4);
        fields.put("usedTime", ValueParser.parseInt(usedTime) + ValueParser.parseInt(testpaperResult.get("usedTime")));

        fields.put("updateTime", System.currentTimeMillis());

        fields.put("endTime", System.currentTimeMillis());
        fields.put("active", 1);

        this.testPaperResultDao.updateTestpaperResultActive(testpaperResult.get("testId"),
                testpaperResult.get("userId"));

        this.testPaperResultDao.updateTestpaperResult(testpaperId, fields);
    }

    @Override
    public List<Map<String, Object>> makeTestpaperResultFinish(Integer id, AppUser currentUser) {
        Map<String, Object> testpaperResult = this.testPaperResultDao.getTestpaperResult(id);

        if (ValueParser.parseInt(testpaperResult.get("userId")) != currentUser.getId()) {
            throw new RuntimeGoingException("无权修改其他学员的试卷！");
        }

        if (ArrayUtil.inArray(testpaperResult.get("status"), "reviewing", "finished")) {
            throw new RuntimeGoingException("已经交卷的试卷不能更改答案!");
        }

        List<Map<String, Object>> listForItems = this.getTestpaperItems(testpaperResult.get("testId"));
        Map<String, Map<String, Object>> items = ArrayToolkit.index(listForItems, "questionId");

        Map<String, Map<String, Object>> questions =
                this.questionService.findQuestionsByIds(ArrayToolkit.column(listForItems, "questionId"));

        //得到当前用户答案
        List<Map<String, Object>> listForAnswers =
                this.testPaperItemResultDao.findTestResultsByTestpaperResultId(ValueParser.toInteger(testpaperResult.get("id")));
        Map<String, Map<String, Object>> answers = ArrayToolkit.index(listForAnswers, "questionId");

        Map<String, Object[]> arrayForAnswers = this.formatAnswers(answers, items);

        answers = this.questionService.judgeQuestions(arrayForAnswers, true);

        answers = this.makeScores(answers, items);

        questions = this.completeQuestion(items, questions);

        for (Map.Entry<String, Map<String, Object>> entry : answers.entrySet()) {
            String questionId = entry.getKey();
            Map<String, Object> answer = entry.getValue();
            if ("noAnswer".equals(answer.get("status"))) {
                answer.put("answer", new String[ArrayUtil.count(questions.get(questionId).get("answer"))]);

                answer.put("testId", testpaperResult.get("testId"));
                answer.put("testPaperResultId", testpaperResult.get("id"));
                answer.put("userId", currentUser.getId());
                answer.put("questionId", questionId);
                this.testPaperItemResultDao.addItemResult(answer);
            }
        }

        //记分
        this.testPaperItemResultDao.updateItemResults(answers, testpaperResult.get("id"));

        return this.testPaperItemResultDao.findTestResultsByTestpaperResultId(ValueParser.toInteger(testpaperResult.get("id")));
    }

    private Map<String, Object[]> formatAnswers(Map<String, Map<String, Object>> answers,
                                                Map<String, Map<String, Object>> items) {

        Map<String, Object[]> results = new HashMap<>();
        for (Map<String, Object> item : items.values()) {
            String questionId = "" + item.get("questionId");
            if (!answers.containsKey(questionId)) {
                results.put(questionId, new Object[0]);
            } else {
                results.put(questionId, ArrayUtil.toArray(answers.get("" + item.get("questionId")).get("answer")));
            }
        }
        return results;
    }

    public Map<String, Map<String, Object>> makeScores(Map<String, Map<String, Object>> answers,
                                                       Map<String, Map<String, Object>> items) {

        for (Map.Entry<String, Map<String, Object>> entry : answers.entrySet()) {
            String questionId = entry.getKey();
            Map<String, Object> answer = entry.getValue();
            if ("right".equals(answer.get("status"))) {
                answer.put("score", items.get(questionId).get("score"));
            } else if ("partRight".equals(answer.get("status"))) {

                if ("fill".equals(items.get(questionId).get("questionType"))) {
                    float score = ValueParser.parseFloat(items.get(questionId).get("score")) *
                            ValueParser.parseInt(answer.get("percentage")) / 100.0F;
                    answer.put("score", score);
                } else {
                    answer.put("score", items.get(questionId).get("missScore"));
                }

            } else {
                answer.put("score", 0);
            }
        }
        return answers;
    }

    @Override
    public Map<String, Object> finishTest(Integer id, Integer userId, Object usedTime) {
        List<Map<String, Object>> itemResults = this.testPaperItemResultDao.findTestResultsByTestpaperResultId(id);

        Map<String, Object> testpaperResult = this.testPaperResultDao.getTestpaperResult(id);

        Map<String, Object> testpaper = this.testPaperDao.getTestpaper(testpaperResult.get("testId"));

        Map<String, Object> fields = new HashMap<>();
        fields.put("status", this.isExistsEssay(itemResults) ? "reviewing" : "finished");

        Map<String, Object> accuracy = this.sumScore(itemResults);
        fields.put("objectiveScore", accuracy.get("sumScore"));

        if (!this.isExistsEssay(itemResults)) {
            fields.put("score", fields.get("objectiveScore"));
        }

        fields.put("rightItemCount", accuracy.get("rightItemCount"));

        int passedScore = ValueParser.parseInt(testpaper.get("passedScore"));
        if (passedScore > 0) {
            fields.put("passedStatus",
                    ValueParser.parseInt(fields.get("score")) >= passedScore ? "passed" : "unpassed");
        } else {
            fields.put("passedStatus", "none");
        }

        fields.put("usedTime", ValueParser.parseInt(usedTime) + ValueParser.parseInt(testpaperResult.get("usedTime")));
        fields.put("endTime", System.currentTimeMillis());
        fields.put("active", 1);
        fields.put("checkedTime", System.currentTimeMillis());

        this.testPaperResultDao.updateTestpaperResultActive(testpaperResult.get("testId"),
                testpaperResult.get("userId"));

        int nums = this.testPaperResultDao.updateTestpaperResult(id, fields);
        if (nums > 0) {
            testpaperResult = this.testPaperResultDao.getTestpaperResult(id);
        }

        this.dispatchEvent(
                "testpaper.finish",
                new ServiceEvent(userService.getUser(userId), testpaper,
                        FastHashMap.build(1).add("testpaperResult", testpaperResult).toMap())
        );

        return testpaperResult;
    }

    private Map<String, Object> sumScore(List<Map<String, Object>> itemResults) {
        float score = 0.0F;
        int rightItemCount = 0;
        for (Map<String, Object> itemResult : itemResults) {
            score += ValueParser.parseFloat(itemResult.get("score"));
            if ("right".equals(itemResult.get("status"))) {
                rightItemCount++;
            }
        }
        return FastHashMap.build(2).add("sumScore", score).add("rightItemCount", rightItemCount).toMap();
    }

    @Override
    public boolean isExistsEssay(List<Map<String, Object>> itemResults) {
        Map<String, Map<String, Object>> questions =
                this.questionService.findQuestionsByIds(ArrayToolkit.column(itemResults, "questionId"));
        for (Map<String, Object> value : questions.values()) {
            if ("essay".equals(value.get("type"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> findAllTestpapersByTargets(Set<Object> courseIds) {
        String[] targets = new String[courseIds == null ? 0 : courseIds.size()];
        if (targets.length > 0) {
            int i = 0;
            for (Object id : courseIds) {
                targets[i++] = "course-" + id;
            }
        }
        return this.testPaperDao.findTestpaperByTargets(targets);
    }

    @Override
    public Map<String, Object> findTestpaperResultsByTestIdAndStatusAndUserId(Integer testpaperId, Integer userId, String[] status) {
        return this.testPaperResultDao.findTestpaperResultsByTestIdAndStatusAndUserId(testpaperId, status, userId);
    }

    @Override
    public Map<String, Object> makeTeacherFinishTest(Integer id, Object paperId, Integer teacherId, Map<String, Object> field) {
        Map<String, Map<String, Object>> testResults = new HashMap<>();

        Object teacherSay = field.get("teacherSay");
        field.remove("teacherSay");


        List<Map<String, Object>> itemsList = this.testPaperItemDao.findItemsByTestpaperId(paperId);
        Map<String, Map<String, Object>> items = ArrayToolkit.index(itemsList, "questionId");

        List<Map<String, Object>> userAnswersList = this.testPaperItemResultDao.findTestResultsByTestpaperResultId(id);
        Map<String, Map<String, Object>> userAnswers = ArrayToolkit.index(userAnswersList, "questionId");

        for (Map.Entry<String, Object> entry : field.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String[] keys = key.split("_");
            if (keys == null || keys.length < 2) {
                continue;
            }

            if (!EasyStringUtil.isNumeric(keys[1])) {
                throw new RuntimeGoingException("得分必须为数字！");
            }

            testResults.computeIfAbsent(keys[1], v -> new HashMap<>(3)).put(keys[0], value);
            Object[] userAnswer = (Object[]) userAnswers.get(keys[1]).get("answer");
            if ("score".equals(keys[0])) {
                if (ValueParser.parseInt(value) == ValueParser.parseFloat(items.get(keys[1]).get("score"))) {
                    testResults.get(keys[1]).put("status", "right");
                } else if (userAnswer == null || "".equals(userAnswer[0])) {
                    testResults.get(keys[1]).put("status", "noAnswer");
                } else {
                    testResults.get(keys[1]).put("status", "wrong");
                }
            }
        }
        //是否要加入教师阅卷的锁
        this.testPaperItemResultDao.updateItemEssays(testResults, id);

        this.questionService.statQuestionTimes(testResults);

        Map<String, Object> testpaperResult = this.testPaperResultDao.getTestpaperResult(id);

        float subjectiveScore = sumTestPaperTotal(testResults.values());

        float totalScore = subjectiveScore + ValueParser.parseFloat(testpaperResult.get("objectiveScore"));

        this.testPaperResultDao.updateTestpaperResult(id, FastHashMap.build(6)
                .add("score", totalScore)
                .add("subjectiveScore", subjectiveScore)
                .add("status", "finished")
                .add("checkTeacherId", teacherId)
                .add("checkedTime", System.currentTimeMillis())
                .add("teacherSay", teacherSay).toMap());
        return this.testPaperResultDao.getTestpaperResult(id);
    }
}
