package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.FastTimeUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller("frontCourseLessonController")
public class CourseLessonController {

    private final CourseService courseService;
    private final AppSettingService settingService;
    private final UserAccessControlService userAccessControlService;

    public CourseLessonController(CourseService courseService,
                                  AppSettingService settingService,
                                  UserAccessControlService userAccessControlService) {

        this.courseService = courseService;
        this.settingService = settingService;
        this.userAccessControlService = userAccessControlService;
    }

    @RequestMapping("/course/{courseId}/lesson/{lessonId}/preview")
    public String previewAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                                HttpServletRequest request, Model model) {

        Map<String, Object> course = this.courseService.getCourse(courseId);
        Map<String, Object> lesson = this.courseService.getCourseLesson(courseId, lessonId);
        AppUser user = AppUser.getCurrentUser(request);

        if (MapUtil.isEmpty(lesson)) {
            throw new RuntimeGoingException("课时信息未找到!");
        }

        if ("closed".equals(course.get("status"))) {
            model.addAttribute("course", course);
            return "/course/lesson/preview-notice-modal";
        }

        if ("1".equals(lesson.get("free"))) {
            String allowAnonymousPreview = settingService.getSettingValue("course.allowAnonymousPreview", "1");
            if (!"1".equals(allowAnonymousPreview) && !userAccessControlService.isLoggedIn()) {
                throw new RuntimeGoingException("由于系统设置未登录用户无法访问免费课时！");
            }
        } else {
            if (!userAccessControlService.isLoggedIn()) {
                throw new RuntimeGoingException("未登录用户无法访问课时！");
            }
//            model.addAttribute("id", courseId);
//            model.addAttribute("preview", Boolean.TRUE);
//            return "forward:/course/order/buy";
        }

        int hasVideoWatermarkEmbedded = 0;
        if ("video".equals(lesson.get("type")) && "self".equals(lesson.get("mediaSource"))) {
            // not finished.
        } else if ("youku".equals(lesson.get("mediaSource"))) {
            // not finished.
        }

        model.addAttribute("user", user);
        model.addAttribute("course", course);
        model.addAttribute("lesson", lesson);
        model.addAttribute("hasVideoWatermarkEmbedded", hasVideoWatermarkEmbedded);
        model.addAttribute("hlsUrl", "");
        return "/course/lesson/preview-modal";
    }

    @RequestMapping("/course/{courseId}/lesson/{lessonId}")
    @ResponseBody
    public Map<String, Object> showAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                                          HttpServletRequest request) {

        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> member = this.courseService.tryTakeCourse(courseId, currentUser);
        Map<String, Object> course = this.courseService.getCourse(courseId);

        Map<String, Object> lesson = this.courseService.getCourseLesson(courseId, lessonId);
        Map<String, Object> json = new HashMap<>();
        json.put("number", lesson.get("number"));

        Map<String, Object> chapter = lesson.get("chapterId") != null ?
                this.courseService.getChapter(courseId, ValueParser.toInteger(lesson.get("chapterId"))) : null;
        if ("unit".equals(chapter.get("type"))) {
            Map<String, Object> unit = chapter;
            json.put("unitNumber", unit.get("number"));

            chapter = this.courseService.getChapter(courseId, ValueParser.toInteger(unit.get("parentId")));
            json.put("chapterNumber", chapter == null ? 0 : chapter.get("number"));

        } else {
            json.put("chapterNumber", chapter == null ? 0 : chapter.get("number"));
            json.put("unitNumber", 0);
        }

        json.put("title", lesson.get("title"));
        json.put("summary", lesson.get("summary"));
        json.put("type", lesson.get("type"));
        json.put("content", lesson.get("content"));
        json.put("status", lesson.get("status"));
        json.put("quizNum", lesson.get("quizNum"));
        json.put("materialNum", lesson.get("materialNum"));
        json.put("mediaId", lesson.get("mediaId"));
        json.put("mediaSource", lesson.get("mediaSource"));
        json.put("startTimeFormat", FastTimeUtil.timeToDateTimeStr("MM-dd HH:mm", ValueParser.parseLong(lesson.get("startTime"))));
        json.put("endTimeFormat", FastTimeUtil.timeToDateTimeStr("HH:mm", ValueParser.parseLong(lesson.get("endTime"))));
        json.put("startTime", lesson.get("startTime"));
        json.put("endTime", lesson.get("endTime"));
        json.put("id", lesson.get("id"));
        json.put("courseId", lesson.get("courseId"));
        json.put("videoWatermarkEmbedded", 0);
        json.put("liveProvider", lesson.get("liveProvider"));

        String appEnabled = this.settingService.getSettingValue("homework.app.enabled", "0");
        if (!"0".equals(appEnabled)) {
//            Map<String, Object> homework = this.homeworkService.getHomeworkByLessonId(lesson.get("id"));
//            Map<String, Object> exercise = this.exerciseService.getExerciseByLessonId(lesson.get("id"));
//            json.put("homeworkOrExerciseNum",  homework.get("itemCount") + exercise.get("itemCount");
        } else {
            json.put("homeworkOrExerciseNum", 0);
        }

        json.put("isTeacher", this.courseService.isCourseTeacher(courseId, currentUser.getId()));
        if ("live".equals(lesson.get("type")) && "generated".equals(lesson.get("replayStatus"))) {
            List<Map<String, Object>> listForReplays = this.courseService.getCourseLessonReplayByLessonId(lesson.get("id"));
            if (listForReplays != null && listForReplays.size() > 0) {
                json.put("replays", listForReplays);
                listForReplays.forEach(e -> e.put("url", BaseControllerHelper.generateUrl(
                        "/livecourse/{courseId}/lesson/{lessonId}/replay/{courseLessonReplayId}/entry",
                        new ParamMap().add("courseId", lesson.get("courseId"))
                                .add("lessonId", lesson.get("id")).add("courseLessonReplayId", e.get("id")).toMap())));
            }
        }

        if ("self".equals(json.get("mediaSource"))) {
            // Map<String, Object> file = this.uploadFileService.getFile(lesson.get("mediaId"));
            Map<String, Object> file = null;
            if (MapUtil.isNotEmpty(file)) {

            } else {
                json.put("mediaUri", "");
                if ("video".equals(lesson.get("type"))) {
                    json.put("mediaError", "抱歉，视频文件不存在，暂时无法学习。");
                } else if ("audio".equals(lesson.get("type"))) {
                    json.put("mediaError", "抱歉，音频文件不存在，暂时无法学习。");
                } else if ("ppt".equals(lesson.get("type"))) {
                    json.put("mediaError", "抱歉，PPT文件不存在，暂时无法学习。");
                }
            }
        } else {
            json.put("mediaUri", lesson.get("mediaUri"));
        }

        json.put("canLearn", this.courseService.canLearnLesson(courseId,
                ValueParser.toInteger(lesson.get("id")), currentUser));

        return json;
    }

    @RequestMapping("/course/{courseId}/lesson/{lessonId}/learn/status")
    @ResponseBody
    public Map<String, Object> learnStatusAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                                                 HttpServletRequest request) {

        AppUser user = AppUser.getCurrentUser(request);
        Object status = this.courseService.getUserLearnLessonStatus(user.getId(), courseId, lessonId);
        Map<String, Object> model = new HashMap<>(1);
        model.put("status", EasyStringUtil.isBlank(status) ? "unstart" : status);
        return model;
    }

    @RequestMapping("/course/{courseId}/lesson/{lessonId}/learn/start")
    @ResponseBody
    public Boolean learnStartAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                                    HttpServletRequest request) {

        AppUser currentUser = AppUser.getCurrentUser(request);
        return this.courseService.startLearnLesson(courseId, lessonId, currentUser);
    }

    @RequestMapping("/course/{courseId}/lesson/{lessonId}/learn/finish")
    @ResponseBody
    public Map<String, Object> learnFinishAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                                                 HttpServletRequest request) {

        AppUser user = AppUser.getCurrentUser(request);
        this.courseService.finishLearnLesson(courseId, lessonId, user);

        Map<String, Object> member = this.courseService.getCourseMember(courseId, user.getId());

        return new ParamMap()
                .add("learnedNum", EasyStringUtil.isBlank(member.get("learnedNum")) ? 0 : member.get("learnedNum"))
                .add("isLearned", EasyStringUtil.isBlank(member.get("isLearned")) ? 0 : member.get("isLearned")).toMap();
    }

    @RequestMapping("/course/{courseId}/lesson/{lessonId}/learn/cancel")
    @ResponseBody
    public Boolean learnCancelAction(@PathVariable Integer courseId, @PathVariable Integer lessonId,
                                     HttpServletRequest request) {

        try {
            this.courseService.cancelLearnLesson(courseId, lessonId, AppUser.getCurrentUser(request));
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
