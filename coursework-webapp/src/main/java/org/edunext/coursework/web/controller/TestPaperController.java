package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.toolbag.ArrayToolkitOnJava8;
import com.jetwinner.util.*;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppNotificationService;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.QuestionService;
import org.edunext.coursework.kernel.service.TestPaperService;
import org.edunext.coursework.kernel.service.question.finder.TargetHelperBean;
import org.edunext.coursework.kernel.service.testpaper.TestPaperExamResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xulixin
 */
@Controller
public class TestPaperController {

    private final CourseService courseService;
    private final TestPaperService testPaperService;
    private final QuestionService questionService;
    private final TargetHelperBean targetHelperBean;
    private final AppUserService userService;
    private final AppNotificationService notificationService;
    private final AppSettingService settingService;

    public TestPaperController(CourseService courseService,
                               TestPaperService testPaperService,
                               QuestionService questionService,
                               TargetHelperBean targetHelperBean,
                               AppUserService userService,
                               AppNotificationService notificationService,
                               AppSettingService settingService) {

        this.courseService = courseService;
        this.testPaperService = testPaperService;
        this.questionService = questionService;
        this.targetHelperBean = targetHelperBean;
        this.userService = userService;
        this.notificationService = notificationService;
        this.settingService = settingService;
    }

    @RequestMapping("/test/{testId}/preview")
    public String previewTestAction(@PathVariable Integer testId, HttpServletRequest request, Model model) {
        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testId);

        if (null == this.testPaperService.canTeacherCheck(testpaper.get("id"), AppUser.getCurrentUser(request))) {
            throw new RuntimeGoingException("无权预览试卷！");
        }

        Map<String, Map<String, Object>> items = this.testPaperService.previewTestpaper(testId);

        model.addAttribute("total", this.makeTestpaperTotal(testpaper, items));

        model.addAttribute("items", items);
        model.addAttribute("limitTime", ValueParser.parseInt(testpaper.get("limitedTime")) * 60);
        model.addAttribute("paper", testpaper);
        model.addAttribute("id", 0);
        model.addAttribute("isPreview", "preview");
        return "/quiz/test/testpaper-show";
    }

    private Map<String, Object> makeTestpaperTotal(Map<String, Object> testpaper,
                                                   Map<String, Map<String, Object>> items) {

        Map testPaperMetas = ArrayToolkit.toMap(testpaper.get("metas"));
        Map<String, Object> total = new HashMap<>(items.keySet().size());
        List<Object> list = (List<Object>) testPaperMetas.get("question_type_seq");
        for (Object obj : list) {
            String type = String.valueOf(obj);
            if (items.get(type) == null) {
                total.put(type, FastHashMap.build(3).add("score", 0).add("number", 0).add("missScore", 0).toMap());
            } else {
                Map<String, Object> map = new HashMap<>(3);
//                map.put("score", array_sum(ArrayToolkit.column(items.get(type), "score"));
                map.put("number", items.get(type).values().size());
                map.put("missScore", 0);
                if (testPaperMetas.containsKey("missScore")) {
                    Map<String, Object> mapForMiss = ArrayToolkit.toMap(testPaperMetas.get("missScore"));
                    if (mapForMiss != null && mapForMiss.containsKey(type)) {
                        map.put("missScore", mapForMiss.get(type));
                    }
                }
                total.put(type, map);
            }
        }
        return total;
    }

    @RequestMapping("/testpaper/{id}/user_result/json")
    @ResponseBody
    public Map<String, Object> userResultJsonAction(@PathVariable Integer id, HttpServletRequest request) {

        AppUser user = AppUser.getCurrentUser(request);
        if (user == null || user.getId() == null) {
            return FastHashMap.build(1).add("error", "您尚未登录系统或登录已超时，请先登录。").toMap();
        }

        Map<String, Object> testpaper = this.testPaperService.getTestpaper(id);
        if (MapUtil.isEmpty(testpaper)) {
            return FastHashMap.build(1).add("error", "试卷已删除，请联系管理员。").toMap();
        }

        Map<String, Object> testResult =
                this.testPaperService.findTestpaperResultByTestpaperIdAndUserIdAndActive(id, user.getId());
        if (MapUtil.isEmpty(testResult)) {
            return FastHashMap.build(1).add("status", "nodo").toMap();
        }

        return FastHashMap.build(2)
                .add("status", testResult.get("status"))
                .add("resultId", testResult.get("id")).toMap();
    }

    @RequestMapping("/test/{testId}/do")
    public ModelAndView doTestpaperAction(@PathVariable Integer testId, HttpServletRequest request) {
        String targetType = request.getParameter("targetType");
        String targetId = request.getParameter("targetId");

        AppUser user = AppUser.getCurrentUser(request);

        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testId);

        Map<String, Map<String, Object>> targets =
                this.targetHelperBean.getTargets(Stream.of(testpaper.get("target")).collect(Collectors.toSet()));

        if (!"course".equals(targets.get(testpaper.get("target")).get("type"))) {
            throw new RuntimeGoingException("试卷只能属于课程");
        }

        Map<String, Object> course = this.courseService.getCourse(ValueParser.toInteger(targets.get(testpaper.get("target")).get("id")));

        if (MapUtil.isEmpty(course)) {
            return BaseControllerHelper.createMessageResponse("info", "试卷所属课程不存在！");
        }

        if (!this.courseService.canTakeCourse(ValueParser.toInteger(course.get("id")), user.getId())) {
            return BaseControllerHelper.createMessageResponse("info", "不是试卷所属课程老师或学生");
        }

        if (MapUtil.isEmpty(testpaper)) {
            throw new RuntimeGoingException("试卷#" + testId + "不存在");
        }

        Map<String, Object> testpaperResult =
                this.testPaperService.findTestpaperResultByTestpaperIdAndUserIdAndActive(testId, user.getId());

        if (MapUtil.isEmpty(testpaperResult)) {

            if ("draft".equals(testpaper.get("status"))) {
                return BaseControllerHelper.createMessageResponse("info", "该试卷未发布，如有疑问请联系老师！");
            }
            if ("closed".equals(testpaper.get("status"))) {
                return BaseControllerHelper.createMessageResponse("info", "该试卷已关闭，如有疑问请联系老师！");
            }

            Map<String, Object> options = FastHashMap.build(2).add("type", targetType).add("id", targetId).toMap();
            AppUser.putCurrentUser(options, user);
            testpaperResult = this.testPaperService.startTestpaper(testId, options);
            return new ModelAndView("redirect:/test/" + testpaperResult.get("id") + "/show");
        }

        if (ArrayUtil.inArray(testpaperResult.get("status"), "doing", "paused")) {
            return new ModelAndView("redirect:/test/" + testpaperResult.get("id") + "/show");
        } else {
            return new ModelAndView("redirect:/test/" + testpaperResult.get("id") + "/result");
        }
    }

    @RequestMapping("/test/{id}/show")
    public String showTestAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> testpaperResult = this.testPaperService.getTestpaperResult(id);
        if (MapUtil.isEmpty(testpaperResult)) {
            throw new RuntimeGoingException("试卷结果没有记录!");
        }
        if (ValueParser.parseInt(testpaperResult.get("userId")) != AppUser.getCurrentUser(request).getId()) {
            throw new RuntimeGoingException("不可以访问其他学生的试卷哦~");
        }
        if (ArrayUtil.inArray(testpaperResult.get("status"), "reviewing", "finished")) {
            return "redirect:/test/" + testpaperResult.get("id") + "/result";
        }

        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testpaperResult.get("testId"));
        TestPaperExamResult result = this.testPaperService.showTestpaper(id);
        Map<String, Map<String, Object>> items = result.getFormatItems();
        model.addAttribute("total", this.makeTestpaperTotal(testpaper, items));

        List<Map<String, Object>> favorites = this.questionService.findAllFavoriteQuestionsByUserId(testpaperResult.get("userId"));
        model.addAttribute("favorites", ArrayToolkit.column(favorites, "questionId"));
        model.addAttribute("items", items);
        model.addAttribute("limitTime", ValueParser.parseInt(testpaperResult.get("limitedTime")) * 60);
        model.addAttribute("paper", testpaper);
        model.addAttribute("paperResult", testpaperResult);
        model.addAttribute("id", id);
        return "/quiz/test/testpaper-show";
    }

    @RequestMapping("/course/{id}/check/{status}/list")
    public String teacherCheckInCourseAction(@PathVariable Integer id, @PathVariable String status,
                                             HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);

        List<Map<String, Object>> testpapers = this.testPaperService.findAllTestpapersByTarget(id);

        Set<Object> testpaperIds = ArrayToolkit.column(testpapers, "id");

        Paginator paginator = new Paginator(request,
                this.testPaperService.findTestpaperResultCountByStatusAndTestIds(testpaperIds, status), 10);

        List<Map<String, Object>> testpaperResults =
                this.testPaperService.findTestpaperResultsByStatusAndTestIds(testpaperIds, status,
                        paginator.getOffsetCount(), paginator.getPerPageCount());

        model.addAttribute("users",
                this.userService.findUsersByIds(ArrayToolkit.column(testpaperResults, "userId")));

        Set<Object> teacherIds = ArrayToolkit.column(testpaperResults, "checkTeacherId");
        List<AppUser> teachers = new ArrayList<>(this.userService.findUsersByIds(teacherIds).values());
        model.addAttribute("teachers", ArrayToolkitOnJava8.index(teachers, AppUser::getId));

        model.addAttribute("status", status);
        model.addAttribute("testpapers", ArrayToolkit.index(testpapers, "id"));
        model.addAttribute("paperResults", ArrayToolkit.index(testpaperResults, "id"));
        model.addAttribute("course", course);
        model.addAttribute("paginator", paginator);
        return "/my/quiz/list-course-test-paper";
    }

    @PostMapping("/test/{id}/submit")
    @ResponseBody
    public Boolean submitTestAction(@PathVariable Integer id, HttpServletRequest request) {
        Map<String, Object> data = EasyWebFormEditor.toFormDataMap(request);
        Map<String, Object> answers = this.mapping("data", data);

        this.testPaperService.submitTestpaperAnswer(id, answers, AppUser.getCurrentUser(request));
        this.testPaperService.updateTestpaperResult(id, data.get("usedTime"));

        return Boolean.TRUE;
    }

    @PostMapping("/test/{id}/suspend")
    @ResponseBody
    public Boolean testSuspendAction(@PathVariable Integer id, HttpServletRequest request) {
        Map<String, Object> testpaperResult = this.testPaperService.getTestpaperResult(id);
        if (MapUtil.isEmpty(testpaperResult)) {
            throw new RuntimeGoingException("试卷不存在!");
        }

        AppUser user = AppUser.getCurrentUser(request);
        //权限！
        if (ValueParser.parseInt(testpaperResult.get("userId")) != user.getId()) {
            throw new RuntimeGoingException("不可以访问其他学生的试卷哦~");
        }

        Map<String, Object> data = EasyWebFormEditor.toFormDataMap(request);
        Map<String, Object> answers = this.mapping("data", data);

        this.testPaperService.submitTestpaperAnswer(id, answers, user);
        this.testPaperService.updateTestpaperResult(id, data.get("usedTime"));

        return Boolean.TRUE;
    }

    private Map<String, Object> mapping(String fieldName, Map<String, Object> fields) {
        if (!fieldName.endsWith("[")) {
            fieldName = fieldName + "[";
        }
        int nums = 0;
        Set<String> keys = fields.keySet();
        for (String key : keys) {
            if (key.startsWith(fieldName)) {
                nums++;
            }
        }
        Map<String, Object> map = new HashMap<>(nums);
        if (nums > 0) {
            for (String key : keys) {
                if (!key.startsWith(fieldName)) {
                    continue;
                }
                String name = key.substring(fieldName.length(), key.length() - 2);
                map.put(name, fields.get(key));
            }
        }
        return map;
    }

    @PostMapping("/test/{id}/finish")
    @ResponseBody
    public Boolean finishTestAction(@PathVariable Integer id, HttpServletRequest request) {

        Map<String, Object> testpaperResult = this.testPaperService.getTestpaperResult(id);

        if (MapUtil.isNotEmpty(testpaperResult) &&
                !ArrayUtil.inArray(testpaperResult.get("status"), "doing", "paused")) {
            return Boolean.TRUE;
        }

        AppUser user = AppUser.getCurrentUser(request);

        Map<String, Object> data = EasyWebFormEditor.toFormDataMap(request);
        Map<String, Object> answers = this.mapping("data", data);


        this.testPaperService.updateTestpaperResult(id, data.get("usedTime"));

        //提交变化的答案
        this.testPaperService.submitTestpaperAnswer(id, answers, user);

        //完成试卷，计算得分
        List<Map<String, Object>> testResults = this.testPaperService.makeTestpaperResultFinish(id, user);

        testpaperResult = this.testPaperService.getTestpaperResult(id);

        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testpaperResult.get("testId"));

        //试卷信息记录
        this.testPaperService.finishTest(id, user.getId(), data.get("usedTime"));

        Map<String, Map<String, Object>> targets = this.targetHelperBean.getTargets(SetUtil.newHashSet(testpaper.get("target")));

        Map<String, Object> course = this.courseService.getCourse(ValueParser.toInteger(targets.get(testpaper.get("target")).get("id")));

        if (this.testPaperService.isExistsEssay(testResults)) {
            String userUrl = request.getContextPath() + "/user/" + user.getId();
            String teacherCheckUrl = request.getContextPath() + "/test/" + testpaperResult.get("id") + "/teacher/check";

            String[] courseTeacherIds = course.get("teacherIds") instanceof String ?
                    CourseService.CourseSerialize.objectToArray(course.get("teacherIds")) : toStringArray(course.get("teacherIds"));
            for (String receiverId : courseTeacherIds) {
                this.notificationService.notify(ValueParser.toInteger(receiverId), "default",
                        "【试卷已完成】 <a href='" + userUrl + "' target='_blank'>" + user.getUsername() +
                                "</a> 刚刚完成了 " + testpaperResult.get("paperName") +
                                " ，<a href='" + teacherCheckUrl +
                                "' target='_blank'>请点击批阅</a>");
            }
        }

        // @todo refactor. , wellming
        targets = targetHelperBean.getTargets(SetUtil.newHashSet(testpaperResult.get("target")));

        Map<String, Object> target = targets.get(testpaperResult.get("target"));
        if (MapUtil.isNotEmpty(target) && "lesson".equals(target.get("type")) &&
                ValueParser.parseInt(target.get("id")) > 0) {

            List<Map<String, Object>> lessons = this.courseService.findLessonsByIds(SetUtil.newHashSet(target.get("id")));
            for (Map<String, Object> lesson : lessons) {
                this.courseService.finishLearnLesson(ValueParser.toInteger(lesson.get("courseId")),
                        ValueParser.toInteger(lesson.get("id")), user);
            }
        }

        return Boolean.TRUE;
    }

    private String[] toStringArray(Object obj) {
        if (obj.getClass().isArray()) {
            Object[] objArray = (Object[]) obj;
            String[] strArray = new String[objArray.length];
            for (int i = 0, len = objArray != null ? objArray.length : 0; i < len; i++) {
                strArray[i] = String.valueOf(objArray[i]);
            }
            return strArray;
        }
        return new String[]{String.valueOf(obj)};
    }

    @RequestMapping("/test/{testId}/redo")
    public ModelAndView reDoTestpaperAction(@PathVariable Integer testId, HttpServletRequest request) {
        String targetType = request.getParameter("targetType");
        String targetId = request.getParameter("targetId");

        AppUser user = AppUser.getCurrentUser(request);

        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testId);

        Map<String, Map<String, Object>> targets =
                this.targetHelperBean.getTargets(Stream.of(testpaper.get("target")).collect(Collectors.toSet()));

        if (!"course".equals(targets.get(testpaper.get("target")).get("type"))) {
            throw new RuntimeGoingException("试卷只能属于课程");
        }

        Map<String, Object> course = this.courseService.getCourse(ValueParser.toInteger(targets.get(testpaper.get("target")).get("id")));

        if (MapUtil.isEmpty(course)) {
            return BaseControllerHelper.createMessageResponse("info", "试卷所属课程不存在！");
        }

        if (!this.courseService.canTakeCourse(ValueParser.toInteger(course.get("id")), user.getId())) {
            return BaseControllerHelper.createMessageResponse("info", "不是试卷所属课程老师或学生");
        }

        if (MapUtil.isEmpty(testpaper)) {
            throw new RuntimeGoingException("试卷#" + testId + "不存在");
        }

        Map<String, Object> testpaperResult =
                this.testPaperService.findTestpaperResultByTestpaperIdAndUserIdAndActive(testId, user.getId());


        Map<String, Object> testResult =
                this.testPaperService.findTestpaperResultsByTestIdAndStatusAndUserId(testId, user.getId(),
                        new String[]{"doing", "paused"});

        if (MapUtil.isNotEmpty(testResult)) {
            return new ModelAndView("redirect:/test/" + testResult.get("id") + "/show");
        }

        if ("draft".equals(testpaper.get("status"))) {
            return BaseControllerHelper.createMessageResponse("info", "该试卷未发布，如有疑问请联系老师！");
        }
        if ("closed".equals(testpaper.get("status"))) {
            return BaseControllerHelper.createMessageResponse("info", "该试卷已关闭，如有疑问请联系老师！");
        }

        Map<String, Object> options = FastHashMap.build(2).add("type", targetType).add("id", targetId).toMap();
        AppUser.putCurrentUser(options, user);
        testResult = this.testPaperService.startTestpaper(testId, options);

        return new ModelAndView("redirect:/test/" + testResult.get("id") + "/show");
    }

    @RequestMapping("/test/{id}/result")
    public String testResultAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> testpaperResult = this.testPaperService.getTestpaperResult(id);
        if (MapUtil.isEmpty(testpaperResult)) {
            throw new RuntimeGoingException("试卷不存在!");
        }

        if (ArrayUtil.inArray(testpaperResult.get("status"), "doing", "paused")) {
            return "redirect:/test/" + testpaperResult.get("id") + "/show";
        }

        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testpaperResult.get("testId"));

        Map<String, Map<String, Object>> targets =
                this.targetHelperBean.getTargets(SetUtil.newHashSet(testpaper.get("target")));

        Map<String, Object> course = null;
        AppUser currentUser = AppUser.getCurrentUser(request);
        if (ValueParser.parseInt(testpaperResult.get("userId")) != currentUser.getId()) {
            course = this.courseService.tryManageCourse(currentUser,
                    ValueParser.toInteger(targets.get(testpaper.get("target")).get("id")));
        }

        if (MapUtil.isEmpty(course) &&
                ValueParser.parseInt(testpaperResult.get("userId")) != currentUser.getId()) {

            throw new RuntimeGoingException("不可以访问其他学生的试卷哦~");
        }

        TestPaperExamResult result = this.testPaperService.showTestpaper(id, true);
        Map<String, Map<String, Object>> items = result.getFormatItems();
        Map<String, Map<String, Integer>> accuracy = result.getAccuracy();

        model.addAttribute("total", this.makeTestpaperTotal(testpaper, items));

        List<Map<String, Object>> favorites =
                this.questionService.findAllFavoriteQuestionsByUserId(testpaperResult.get("userId"));
        model.addAttribute("favorites", ArrayToolkit.column(favorites, "questionId"));

        model.addAttribute("student", this.userService.getUser(testpaperResult.get("userId")));

        model.addAttribute("id", id);
        model.addAttribute("paperResult", testpaperResult);
        model.addAttribute("paper", testpaper);
        model.addAttribute("accuracy", accuracy);
        model.addAttribute("items", items);

        return "/quiz/test/testpaper-result";
    }

    @GetMapping("/test/{id}/teacher/check")
    public String teacherCheckPage(@PathVariable Integer id, HttpServletRequest request, Model model) {
        //身份校验?

        Map<String, Object> testpaperResult = this.testPaperService.getTestpaperResult(id);

        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testpaperResult.get("testId"));


        if (null == this.testPaperService.canTeacherCheck(testpaper.get("id"), AppUser.getCurrentUser(request))) {
            throw new RuntimeGoingException("无权批阅试卷！");
        }

        if (!"reviewing".equals(testpaperResult.get("status"))) {
            return "redirect:/test/" + testpaperResult.get("id") + "/result";
        }

        TestPaperExamResult result = this.testPaperService.showTestpaper(id, true);
        Map<String, Map<String, Object>> items = result.getFormatItems();
        Map<String, Map<String, Integer>> accuracy = result.getAccuracy();

        model.addAttribute("total", this.makeTestpaperTotal(testpaper, items));

        List<String> types = new ArrayList<>();
        Map<String, Object> metasMap = ArrayToolkit.toMap(testpaper.get("metas"));
        List<Object> questionTypeSequence = (List<Object>) (metasMap.get("question_type_seq"));
        String[] arrayQuestionTypes = questionTypeSequence.stream().toArray(String[]::new);
        if (ArrayUtil.inArray("essay", arrayQuestionTypes)) {
            types.add("essay");
        }
        if (ArrayUtil.inArray("material", arrayQuestionTypes)) {

            for (Map.Entry<String, Object> entry : items.get("material").entrySet()) {
                String key = entry.getKey();
                Map<String, Object> item = ArrayToolkit.toMap(entry.getValue());

                List<Map<String, Object>> listItems = (List<Map<String, Object>>) (item.get("items"));
                Map<String, Map<String, Object>> questionTypes = ArrayToolkit.index(listItems == null ? new ArrayList<>(0) :
                        listItems, "questionType");

                if (questionTypes.containsKey("essay")) {
                    if (!types.contains("material")) {
                        types.add("material");
                    }
                }
            }
        }
        model.addAttribute("types", types);

        model.addAttribute("student", this.userService.getUser(testpaperResult.get("userId")));

        model.addAttribute("questionsSetting", this.settingService.get("questions"));
        model.addAttribute("items", items);
        model.addAttribute("accuracy", accuracy);
        model.addAttribute("paper", testpaper);
        model.addAttribute("paperResult", testpaperResult);
        model.addAttribute("id", id);

        return "/quiz/test/testpaper-review";
    }

    @PostMapping("/test/{id}/teacher/check")
    @ResponseBody
    public Boolean teacherCheckAction(@PathVariable Integer id, HttpServletRequest request) {
        Map<String, Object> testpaperResult = this.testPaperService.getTestpaperResult(id);

        Map<String, Object> testpaper = this.testPaperService.getTestpaper(testpaperResult.get("testId"));

        Map<String, Object> form = EasyWebFormEditor.toFormDataMap(request);
        Integer teacherId = this.testPaperService.canTeacherCheck(testpaper.get("id"), AppUser.getCurrentUser(request));
        if (null == teacherId) {
            throw new RuntimeGoingException("无权批阅试卷！");
        }

        testpaperResult = this.testPaperService.makeTeacherFinishTest(id, testpaper.get("id"), teacherId, form);

        AppUser user = AppUser.getCurrentUser(request);

        String userUrl = request.getContextPath() + "/user/" + user.getId();
        String testpaperResultUrl = request.getContextPath() + "/test/" + testpaperResult.get("id") + "/result";


        this.notificationService.notify(ValueParser.toInteger(testpaperResult.get("userId")), "default",
                "【试卷已批阅】 <a href='" + userUrl + "' target='_blank'>" + user.getUsername() +
                        "</a> 刚刚批阅了 " + testpaperResult.get("paperName") +
                        " ，<a href='" + testpaperResultUrl +
                        "' target='_blank'>请点击查看结果</a>");

        return Boolean.TRUE;
    }
}
