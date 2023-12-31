package org.edunext.coursework.web.controller.admin;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.kernel.view.ViewRenderService;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import org.edunext.coursework.kernel.CategoryBuilder;
import org.edunext.coursework.kernel.service.CourseCopyService;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.TestPaperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xulixin
 */
@Controller("adminCourseController")
public class CourseController {

    private final CourseService courseService;
    private final CourseCopyService courseCopyService;
    private final TestPaperService testPaperService;
    private final CategoryBuilder categoryBuilder;
    private final AppCategoryService categoryService;
    private final AppUserService userService;
    private final AppSettingService settingService;
    private final ViewRenderService viewRenderService;

    public CourseController(CourseService courseService,
                            CourseCopyService courseCopyService,
                            TestPaperService testPaperService,
                            CategoryBuilder categoryBuilder,
                            AppCategoryService categoryService,
                            AppUserService userService,
                            AppSettingService settingService,
                            ViewRenderService viewRenderService) {

        this.courseService = courseService;
        this.courseCopyService = courseCopyService;
        this.testPaperService = testPaperService;
        this.categoryBuilder = categoryBuilder;
        this.categoryService = categoryService;
        this.userService = userService;
        this.settingService = settingService;
        this.viewRenderService = viewRenderService;
    }

    @RequestMapping("/admin/course")
    public String indexPage(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = ParamMap.toQueryAllMap(request);
        int count = courseService.searchCourseCount(conditions);

        Paginator paginator = new Paginator(request, count, 20);
        List<Map<String, Object>> courses = courseService.searchCourses(conditions, null,
                paginator.getOffsetCount(),  paginator.getPerPageCount());

        model.addAttribute("categories",
                categoryService.findCategoriesByIds(ArrayToolkit.column(courses, "categoryId")));

        model.addAttribute("users", userService.findUsersByIds(ArrayToolkit.column(courses, "userId")));

        Map<String, Object> courseSetting = settingService.get("course");
        if (Objects.isNull(courseSetting.get("live_course_enabled"))) {
            courseSetting.put("live_course_enabled", "");
        }

        model.addAttribute("conditions", conditions);
        model.addAttribute("courses", courses);
        model.addAttribute("paginator", paginator);
        model.addAttribute("liveSetEnabled", courseSetting.get("live_course_enabled"));
        model.addAttribute("default", settingService.get("default"));
        model.addAttribute("categoryForCourse", categoryService.buildCategoryChoices("course"));

        return "/admin/course/index";
    }

    @RequestMapping("admin/course/{id}/recommend")
    @ResponseBody
    public String recommendAction(HttpServletRequest request, @PathVariable Integer id) {
        Map<String, Object> course = courseService.getCourse(id);

        String ref = request.getParameter("ref");

        if ("POST".equals(request.getMethod())) {
            String number = request.getParameter("number");
            course = courseService.recommendCourse(AppUser.getCurrentUser(request), id, number);

            AppUser user = userService.getUser(course.get("userId"));

            if ("recommendList".equals(ref)) {
                return viewRenderService.renderView(request, "/admin/course/course-recommend-tr.ftl",
                        new ParamMap().add("course", course).add("user", user).toMap());
            }

            return this.renderCourseTr(request, id);
        }


        return viewRenderService.renderView(request, "/admin/course/course-recommend-modal.ftl",
                new ParamMap().add("course", course).add("ref", ref).toMap());
    }

    private String renderCourseTr(HttpServletRequest request, Integer courseId) {
        Map<String, Object> course = courseService.getCourse(courseId);
        return viewRenderService.renderView(request, "/admin/course/tr.ftl",
                new ParamMap().add("user", userService.getUser(course.get("userId")))
                        .add("category", categoryService.getCategory(ValueParser.toInteger(course.get("categoryId"))))
                        .add("default", settingService.get("default"))
                        .add("course", course).toMap());
    }

    @RequestMapping("/admin/course/{id}/recommend/cancel")
    @ResponseBody
    public String cancelRecommendAction(HttpServletRequest request, @PathVariable Integer id) {
        courseService.cancelRecommendCourse(AppUser.getCurrentUser(request), id);
        return this.renderCourseTr(request, id);
    }

    @RequestMapping("/admin/course/recommend/list")
    public String recommendListAction(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = new ParamMap()
                .add("status", "published").add("recommended", 1).toMap();

        Paginator paginator = new Paginator(request, courseService.searchCourseCount(conditions), 20);

        List<Map<String, Object>> courses = courseService.searchCourses(conditions, "recommendedSeq",
                paginator.getOffsetCount(), paginator.getPerPageCount());

        model.addAttribute("courses", courses);
        model.addAttribute("users", userService.findUsersByIds(ArrayToolkit.column(courses, "userId")));
        model.addAttribute("paginator", paginator);

        return "/admin/course/course-recommend-list";
    }

    @RequestMapping("/admin/course/{id}/publish")
    @ResponseBody
    public String publishAction(HttpServletRequest request, @PathVariable Integer id) {
        courseService.publishCourse(AppUser.getCurrentUser(request), id);
        return this.renderCourseTr(request, id);
    }

    @RequestMapping("/admin/course/{id}/close")
    @ResponseBody
    public String closeAction(HttpServletRequest request, @PathVariable Integer id) {
        courseService.closeCourse(AppUser.getCurrentUser(request), id);
        return this.renderCourseTr(request, id);
    }

    @RequestMapping("/admin/course/{id}/copy")
    @ResponseBody
    public String copyAction(HttpServletRequest request, @PathVariable Integer id) {
        return viewRenderService.renderView(request, "/admin/course/copy.ftl",
                new ParamMap().add("course", courseService.getCourse(id)).toMap());
    }

    @RequestMapping("/admin/course/{id}/copying")
    public String copingAction(HttpServletRequest request, @PathVariable Integer id) {
        Map<String, Object> course = courseService.getCourse(id);
        course.put("title", request.getParameter("title"));
        Map<String, Object> newCourse = courseCopyService.copyCourse(course);

//        courseCopyService.copyTeachers(course.get("id"), newCourse);

//        List<Map<String, Object>> newChapters = courseCopyService.copyChapters(course.get("id"), newCourse);

//        List<Map<String, Object>> newLessons = courseCopyService.copyLessons(course.get("id"), newCourse, newChapters);

//        List<Map<String, Object>> newQuestions = courseCopyService.copyQuestions(course.get("id"), newCourse, newLessons);

//        List<Map<String, Object>> newTestpapers = courseCopyService.copyTestpapers(course.get("id"), newCourse, newQuestions);

//        courseCopyService.convertTestpaperLesson(newLessons, newTestpapers);

//        courseCopyService.copyMaterials(course.get("id"), newCourse, newLessons);

        boolean isCopyHomework = "1".equals(settingService.getSettingValue("course.homework.enabled"));
        if (isCopyHomework) {
//            courseCopyService.copyHomeworks(course.get("id"), newCourse, newLessons, newQuestions);
//            courseCopyService.copyExercises(course.get("id"), newCourse, newLessons);
        }

        return "redirect:/admin/course";
    }

    @RequestMapping("/admin/course/{id}/delete")
    @ResponseBody
    public Boolean deleteAction(HttpServletRequest request, @PathVariable Integer id) {
        boolean result = courseService.deleteCourse(AppUser.getCurrentUser(request), id);
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    @RequestMapping("/admin/course/data")
    public String dataAction(HttpServletRequest request, Model model) {
        Map<String, Object> conditions = ParamMap.toConditionMap(request);
        conditions.put("type", "normal");
        int count = courseService.searchCourseCount(conditions);

        Paginator paginator = new Paginator(request, count, 20);

        List<Map<String, Object>> courses = courseService.searchCourses(conditions, null,
                paginator.getOffsetCount(),  paginator.getPerPageCount());

        for (Map<String, Object> course : courses) {
            Integer isLearnedNum= courseService.searchMemberCount(new ParamMap()
                    .add("isLearned", 1).add("courseId", course.get("id")).toMap());

            Integer learnTime = courseService.searchLearnTime(new ParamMap()
                    .add("courseId", course.get("id")).toMap());

            Integer lessonCount = courseService.searchLessonCount(new ParamMap()
                    .add("courseId", course.get("id")).toMap());

            course.put("isLearnedNum", isLearnedNum);
            course.put("learnTime", learnTime);
            course.put("lessonCount", lessonCount);
        }

        model.addAttribute("categoryChoiceCourse", categoryBuilder.buildChoices("course"));
        model.addAttribute("courses", courses);
        model.addAttribute("paginator", paginator);
        return "/admin/course/data";
    }

    @RequestMapping("/admin/course/{id}/lesson/data")
    public String lessonDataAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), id);

        List<Map<String, Object>> lessons =
                this.courseService.searchLessons(FastHashMap.build(1).add("courseId", id).toMap(),
                        OrderBy.build(1).addAsc("createdTime"), 0, 1000);

        for (Map<String, Object> value : lessons) {
            int lessonLearnedNum = this.courseService.findLearnsCountByLessonId(ValueParser.toInteger(value.get("id")));
            Integer finishedNum =
                    this.courseService.searchLearnCount(FastHashMap.build(2).add("status", "finished").add("lessonId", value.get("id")).toMap());

            int lessonLearnTime = this.courseService.searchLearnTime(FastHashMap.build(1).add("lessonId", value.get("id")).toMap());
            lessonLearnTime = lessonLearnedNum == 0 ? 0 : (int) (1.0 * lessonLearnTime / lessonLearnedNum);

            int lessonWatchTime = this.courseService.searchWatchTime(FastHashMap.build(1).add("lessonId", value.get("id")).toMap());
            lessonWatchTime = lessonWatchTime == 0 ? 0 : (int) (1.0 * lessonWatchTime / lessonLearnedNum);

            value.put("LearnedNum", lessonLearnedNum);
            value.put("length", (int) (ValueParser.parseInt(value.get("length")) / 60.0));
            value.put("finishedNum", finishedNum);
            value.put("learnTime", lessonLearnTime);
            value.put("watchTime", lessonWatchTime);

            if ("testpaper".equals(value.get("type"))) {
                Integer paperId = ValueParser.toInteger(value.get("mediaId"));
                int score = this.testPaperService.searchTestpapersScore(
                        FastHashMap.build(1).add("testId", paperId).toMap());

                int paperNum = this.testPaperService.searchTestpaperResultsCount(
                        FastHashMap.build(1).add("testId", paperId).toMap());

                value.put("score", finishedNum == 0 ? 0 : (int) (1.0 * score / paperNum));
            }
        }

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);

        return "admin/course/lesson-data";
    }
}
