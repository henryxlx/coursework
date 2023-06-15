package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.*;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
            String keyForMediaId = String.valueOf(item.get("mediaId"));
            if (!mediaMap.containsKey(keyForMediaId)) {
                mediaMap.put(keyForMediaId, new HashSet<>());
            }
            mediaMap.get(keyForMediaId).add(item.get("id"));
        }

        Set<String> mediaIds = mediaMap.keySet();

//        List<Map<String, Object>> files = uploadFileService.findFilesByIds(mediaIds);
        Map<String, Map<String, Object>> mapForCourseItems = MapUtil.newHashMap(courseItems.size());
        courseItems.forEach(e -> {
            mapForCourseItems.put(e.get("itemType") + "-" + e.get("id"), e);
        });
        List<Map<String, Object>> files = ListUtil.newArrayList();
        for (Map<String, Object> file : files) {
            lessonIds = mediaMap.get(file.get("id"));
            for (Object lessonId : lessonIds) {
                Map<String, Object> val = mapForCourseItems.get("lesson-" + lessonId);
                if (val != null) {
                    val.put("mediaStatus", file.get("convertStatus"));
                }
            }
        }
        model.addAttribute("files", ArrayToolkit.index(files, "id"));
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

    @RequestMapping("/course/{courseId}/manage/lesson/{lessonId}/edit")
    public String editAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                             HttpServletRequest request, Model model) {

        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> course = this.courseService.tryManageCourse(currentUser, courseId);
        Map<String, Object> lesson = this.courseService.getCourseLesson(courseId, lessonId);
        if (MapUtil.isEmpty(lesson)) {
            throw new RuntimeGoingException("课时(#" + lessonId + ")不存在！");
        }

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> fields = ParamMap.toQueryAllMap(request);
            if (EasyStringUtil.isNotBlank(fields.get("media"))) {
                fields.put("media", JsonUtil.jsonDecode(fields.get("media"), true));
            }

            if (EasyStringUtil.isNotBlank(fields.get("second"))) {
                fields.put("length", this.textToSeconds(fields.get("minute"), fields.get("second")));
                fields.remove("minute");
                fields.remove("second");
            }

            fields.put("free", EasyStringUtil.isBlank(fields.get("free")) ? 0 : 1);
            lesson = this.courseService.updateLesson(courseId, lessonId, fields, currentUser);
            this.courseService.deleteCourseDrafts(courseId, lessonId, currentUser.getId());

            Map<String, Object> file = null;
            if (ValueParser.parseInt(lesson.get("mediaId")) > 0 && !"testpaper".equals(lesson.get("type"))) {
//                file = this.uploadFileService.getFile(lesson.get("mediaId"));
                lesson.put("mediaStatus", file.get("convertStatus"));
                if ("document".equals(file.get("type")) && "none".equals(file.get("convertStatus"))) {
//                    this.uploadFileService.reconvertFile(file.get("id"),
//                            this.generateUrl("uploadfile_cloud_convert_callback2", new HashMap<>(0), true));
                }
            }

            model.addAttribute("course", course);
            model.addAttribute("lesson", lesson);
            model.addAttribute("file", file);
            return "/course/manage/lesson/list-item";
        }

        Map<String, Object> file = null;
        if (lesson.get("mediaId") != null) {
//            file = this.uploadFileService.getFile(lesson.get("mediaId"));
            if (MapUtil.isNotEmpty(file)) {
                lesson.put("media", new ParamMap()
                        .add("id", file.get("id"))
                        .add("status", file.get("convertStatus"))
                        .add("source", "self")
                        .add("name", file.get("filename")).add("uri", "").toMap());
            } else {
                lesson.put("media", new ParamMap().add("id", 0).add("status", "none").add("source", "")
                        .add("name", "文件已删除").add("uri", "").toMap());
            }
        } else {
            lesson.put("media", new ParamMap().add("id", 0).add("status", "none").add("source", lesson.get("mediaSource"))
                    .add("name", lesson.get("mediaName")).add("uri", lesson.get("mediaUri")).toMap());
        }

        this.secondsToText(lesson);
        String lessonTitle = lesson.get("title") == null ? null : String.valueOf(lesson.get("title"));
        if (EasyStringUtil.isNotBlank(lessonTitle)) {
            lessonTitle = lessonTitle.replaceAll("\"", "'");
            lessonTitle = lessonTitle.replaceAll("&#34;", "&#39;");
            lesson.put("title", lessonTitle);
        }

        String randString = FastEncryptionUtil.sha1(String.valueOf(new Random().nextInt())).substring(0, 12);
        String filePath = "courselesson/" + courseId;
        model.addAttribute("filePath", filePath);
        model.addAttribute("fileKey", filePath + randString);
        model.addAttribute("convertKey", randString);

        Map<String, Object> features = this.settingService.get("enabled_features");
        model.addAttribute("features", MapUtil.isEmpty(features) ? new ArrayList<>(0) : features.values());

        model.addAttribute("draft", this.courseService.findCourseDraft(courseId, lessonId, currentUser.getId()));
        model.addAttribute("storageSetting", this.settingService.get("storage"));
        model.addAttribute("course", course);
        model.addAttribute("lesson", lesson);
        model.addAttribute("file", file);
        model.addAttribute("targetType", "courselesson");
        model.addAttribute("targetId", courseId);
        return "/course/manage/lesson/lesson-modal";
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

    @RequestMapping("/course/{id}/manage/lesson/sort")
    @ResponseBody
    public Boolean sortAction(HttpServletRequest request, @PathVariable Integer id) {
        String[] ids = request.getParameterValues("ids[]");
        if (ids != null && ids.length > 0) {
            Map<String, Object> course = this.courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
            this.courseService.sortCourseItems(course.get("id"), ids);
        }
        return Boolean.TRUE;
    }

    @RequestMapping("/course/{courseId}/manage/lesson/{lessonId}/delete")
    @ResponseBody
    public Boolean deleteAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                                HttpServletRequest request) {

        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> course = this.courseService.tryManageCourse(currentUser, courseId);
        Map<String, Object> lesson = this.courseService.getCourseLesson(courseId, lessonId);
        if ("live".equals(course.get("type"))) {
//            this.courseService.deleteCourseLessonReplayByLessonId(lessonId);
        }
        this.courseService.deleteLesson(courseId, lessonId, currentUser);
//        this.courseMaterialService.deleteMaterialsByLessonId(lessonId);

        return Boolean.TRUE;
    }
}
