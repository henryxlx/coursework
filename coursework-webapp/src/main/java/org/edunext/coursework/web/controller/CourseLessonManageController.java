package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.*;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author xulixin
 */
@Controller
public class CourseLessonManageController {

    private final CourseService courseService;
    private final AppSettingService settingService;

    public CourseLessonManageController(CourseService courseService,
                                        AppSettingService settingService) {

        this.courseService = courseService;
        this.settingService = settingService;
    }

    @RequestMapping("/course/{id}/manage/lesson")
    public String indexAction(HttpServletRequest request, @PathVariable Integer id, Model model) {
        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        List<Map<String, Object>> courseItems = courseService.getCourseItems(course.get("id"));

        Set<Object> lessonIds = ArrayToolkit.column(courseItems, "id");

        if ("1".equals(settingService.getSettingValue("homework.enabled"))) {
//            model.addAttribute("exercises", "Homework.ExerciseService.findExercisesByLessonIds(lessonIds);
//            model.addAttribute("homeworks", "Homework.HomeworkService.findHomeworksByCourseIdAndLessonIds(course.get("id"), lessonIds);
        }

        Map<String, Set<Object>> mediaMap = new HashMap<>();
        for (Map<String, Object> item : courseItems) {
            if (!"lesson".equals(item.get("itemType"))) {
                continue;
            }

            if (EasyStringUtil.isBlank(item.get("mediaId"))) {
                continue;
            }

            mediaMap.putIfAbsent(String.valueOf(item.get("mediaId")), new HashSet<>()).add(item.get("id"));
        }

        Set<String> mediaIds = mediaMap.keySet();

/*
        List<Map<String, Object>> files = uploadFileService.findFilesByIds(mediaIds);
        for (Map<String, Object> file : files) {
            lessonIds = mediaMap.get(file.get("id"));
            for (Object lessonId : lessonIds) {
                courseItems.get("lesson-" + lessonId).put("mediaStatus", file.get("convertStatus"));
            }
        }
        model.addAttribute("files", ArrayToolkit.index(files, "id"));
*/
        model.addAttribute("course", course);
        model.addAttribute("items", courseItems);
        model.addAttribute("default", settingService.get("default"));
        return "/course/manage/lesson/index";
    }


    @RequestMapping("/course/{id}/manage/lesson/create")
    public String createAction(HttpServletRequest request, @PathVariable Integer id, Model model) {
        AppUser user = AppUser.getCurrentUser(request);
        Map<String, Object> course = courseService.tryManageCourse(user, id);
        String parentId = request.getParameter("parentId");

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> lesson = ParamMap.toQueryAllMap(request);
            lesson.put("courseId", course.get("id"));

            if (EasyStringUtil.isNotBlank(lesson.get("media"))) {
                lesson.put("media", JsonUtil.jsonDecode(lesson.get("media"), true));
            }
            if (EasyStringUtil.isNumeric(String.valueOf(lesson.get("second")))) {
                lesson.put("length", this.textToSeconds(lesson.get("minute"), lesson.get("second")));
                lesson.remove("minute");
                lesson.remove("second");
            }
            lesson = this.courseService.createLesson(lesson, user);

            Map<String, Object> file = MapUtil.newHashMap(0);
            if (ValueParser.parseInt(lesson.get("mediaId")) > 0 && (!"testpaper".equals(lesson.get("type")))) {
//                file = this.getUploadFileService.getFile(lesson.get("mediaId"));
//                lesson.put("mediaStatus", file.get("convertStatus"));
            }
            Integer lessonId = 0;
            this.courseService.deleteCourseDrafts(id, lessonId, user.getId());

            model.addAttribute("course", course);
            model.addAttribute("lesson", lesson);
            model.addAttribute("file", file);
            return "/course/manage/lesson/list-item";
        }

        Integer userId = user.getId();
        String randString = FastEncryptionUtil.sha1(String.valueOf(new Random().nextInt())).substring(0, 12);
        String filePath = "courselesson/" + course.get("id");
        String fileKey = filePath + randString;
        String convertKey = randString;

        String targetType = "courselesson";
        Integer targetId = ValueParser.toInteger(course.get("id"));

        Map<String, Object> features = settingService.get("enabled_features");

        model.addAttribute("course", course);
        model.addAttribute("targetType", targetType);
        model.addAttribute("targetId", targetId);
        model.addAttribute("filePath", filePath);
        model.addAttribute("fileKey", fileKey);
        model.addAttribute("convertKey", convertKey);
        model.addAttribute("storageSetting", settingService.get("storage"));
        model.addAttribute("features", features.values());
        model.addAttribute("parentId", parentId);
        model.addAttribute("draft", courseService.findCourseDraft(targetId, 0, userId));
        return "/course/manage/lesson/lesson-modal";
    }

    private int textToSeconds(Object minutes, Object seconds) {
        int m = ValueParser.parseInt(minutes);
        int s = ValueParser.parseInt(seconds);
        return m * 60 + s;
    }

    private void secondsToText(Map<String, Object> lesson) {
        int value = ValueParser.parseInt(lesson.get("length"));
        int minutes = value / 60;
        int seconds = value - minutes * 60;
        lesson.put("minute", minutes);
        lesson.put("second", seconds);
    }

    @RequestMapping("/course/{id}/manage/lesson/create/testpaper")
    public String createTestPaperAction(HttpServletRequest request, @PathVariable Integer id, Model model) {
        AppUser user = AppUser.getCurrentUser(request);
        Map<String, Object> course = courseService.tryManageCourse(user, id);
        String parentId = request.getParameter("parentId");
        Map<String, Object> conditions = new ParamMap()
                .add("target", "course-" + course.get("id"))
                .add("status", "open").toMap();

//        List<Map<String, Object>> testpapers = this.testpaperService.searchTestpapers(conditions,
//                OrderBy.build(1).addDesc("createdTime"), 0, 1000);
        List<Map<String, Object>> testpapers = new ArrayList<>(0);

        Map<String, Object> paperOptions = null;
        if (testpapers != null && testpapers.size() > 0) {
            paperOptions = MapUtil.newHashMap(testpapers.size());
            for (Map<String, Object> testpaper : testpapers) {
                paperOptions.put(String.valueOf(testpaper.get("id")), testpaper.get("name"));
            }
        }

        if ("POST".equals(request.getMethod())) {

            Map<String, Object> lesson = ParamMap.toQueryAllMap(request);
            lesson.put("type", "testpaper");
            lesson.put("courseId", course.get("id"));
            lesson = this.courseService.createLesson(lesson, user);
            model.addAttribute("course", course);
            model.addAttribute("lesson", lesson);
            return "/course/manage/lesson/list-item";
        }

        Map<String, Object> features = settingService.get("enabled_features");
        model.addAttribute("course", course);
        model.addAttribute("paperOptions", paperOptions);
        model.addAttribute("features", features.values());
        model.addAttribute("parentId", parentId);
        return "/course/manage/lesson/testpaper-modal";
    }
}
