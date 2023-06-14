package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.ActionGraspException;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.*;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Controller
public class CourseStudentManageController {

    private final CourseService courseService;
    private final AppUserService userService;
    private final AppUserFieldService userFieldService;
    private final AppNotificationService notificationService;
    private final AppSettingService settingService;
    private final UserAccessControlService userAccessControlService;
    private final AppLogService logService;

    public CourseStudentManageController(CourseService courseService,
                                         AppUserService userService,
                                         AppUserFieldService userFieldService,
                                         AppNotificationService notificationService,
                                         AppSettingService settingService,
                                         UserAccessControlService userAccessControlService,
                                         AppLogService logService) {

        this.courseService = courseService;
        this.userService = userService;
        this.userFieldService = userFieldService;
        this.notificationService = notificationService;
        this.settingService = settingService;
        this.userAccessControlService = userAccessControlService;
        this.logService = logService;
    }

    @RequestMapping("/course/{id}/manage/student")
    public String indexAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> course = courseService.tryManageCourse(currentUser, id);

        Map<String, Object> fields = ParamMap.toQueryAllMap(request);
        String username = null;
        if (EasyStringUtil.isNotBlank(fields.get("username"))) {
            username = fields.get("username").toString();
        }

        Map<String, Object> conditions = new ParamMap().add("courseId", course.get("id")).add("role", "student").toMap();
        if (username != null) {
            conditions.put("username", username);
        }
        Paginator paginator = new Paginator(request, this.courseService.searchMemberCount(conditions), 20);

        List<Map<String, Object>> students = this.courseService.searchMembers(conditions,
                OrderBy.build(1).addDesc("createdTime"),
                paginator.getOffsetCount(),
                paginator.getPerPageCount());
        Set<Object> studentUserIds = ArrayToolkit.column(students, "userId");
        model.addAttribute("users", this.userService.findUsersByIds(studentUserIds));
        model.addAttribute("followingIds", this.userService.filterFollowingIds(currentUser.getId(), studentUserIds));

        Map<String, Object> progresses = MapUtil.newHashMap(students.size());
        students.forEach(student -> progresses.put(String.valueOf(student.get("userId")),
                this.calculateUserLearnProgress(course, student)));
        model.addAttribute("progresses", progresses);

        Map<String, Object> courseSetting = this.settingService.get("course");
        int isTeacherAuthManageStudent = EasyStringUtil.isNotBlank(courseSetting.get("teacher_manage_student")) ? 1 : 0;
        model.addAttribute("isTeacherAuthManageStudent", isTeacherAuthManageStudent);
        model.addAttribute("default", this.settingService.get("default"));
        model.addAttribute("canManage", this.courseService.canManageCourse(ValueParser.toInteger(course.get("id")),
                currentUser.getId()));

        model.addAttribute("course", course);
        model.addAttribute("students", students);
        model.addAttribute("paginator", paginator);
        return "/course/manage/student/index";
    }

    private Map<String, Object> calculateUserLearnProgress(Map<String, Object> course, Map<String, Object> member) {
        if (ValueParser.parseInt(course.get("lessonNum")) == 0) {
            return new ParamMap().add("percent", "0%").add("number", 0).add("total", 0).toMap();
        }

        String percent = "0%";
        int memberLearnedNum = ValueParser.parseInt(member.get("learnedNum"));
        int courseLessonNum = ValueParser.parseInt(course.get("lessonNum"));
        if (memberLearnedNum > 0 && courseLessonNum > 0) {
            percent = (int) (1.0 * memberLearnedNum / courseLessonNum * 100) + "%";
        }

        return new ParamMap().add("percent", percent)
                .add("number", member.get("learnedNum"))
                .add("total", course.get("lessonNum")).toMap();
    }

    @RequestMapping("/course/{id}/manage/student/create")
    public String createAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> course;

        Map<String, Object> courseSetting = this.settingService.get("course");
        if (EasyStringUtil.isNotBlank(courseSetting.get("teacher_manage_student"))) {
            course = this.courseService.tryManageCourse(currentUser, id);
        } else {
            course = this.courseService.tryAdminCourse(currentUser, id);
        }

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> data = ParamMap.toQueryAllMap(request);
            AppUser user = this.userService.getUserByUsername(String.valueOf(data.get("username")));
            if (user == null) {
                throw new RuntimeGoingException("用户" + data.get("username") + "不存在");
            }

            if (this.courseService.isCourseStudent(ValueParser.toInteger(course.get("id")), user.getId())) {
                throw new RuntimeGoingException("用户已经是学员，不能添加！");
            }

            Map<String, Object> info = new ParamMap().add("orderId", 0).add("note", data.get("remark")).toMap();

            try {
                this.courseService.becomeStudent(course, id, user.getId(), info);
            } catch (ActionGraspException e) {
                throw new RuntimeGoingException(e.getMessage());
            }

            Map<String, Object> member = this.courseService.getCourseMember(id, user.getId());

            this.notificationService.notify(ValueParser.toInteger(member.get("userId")), "student-create",
                    new ParamMap().add("courseId", course.get("id")).add("courseTitle", course.get("title")).toMap());

            this.logService.info(currentUser, "course", "add_student",
                    String.format("课程《%s》(#%s)，添加学员%s(#%s)，备注：%s", course.get("title"), course.get("id"),
                            user.getUsername(), user.getId(), data.get("remark")));

            return this.createStudentTrResponse(course, member, currentUser, model);
        }

        model.addAttribute("default", this.settingService.get("default"));
        model.addAttribute("course", course);
        return "/course/manage/student/create-modal";
    }

    @RequestMapping("/course/{id}/manage/username_check")
    @ResponseBody
    public Map<String, Object> checkUsernameAction(@PathVariable Integer id,
                                                   @RequestParam(name = "value") String username) {

        boolean result = this.userService.isUsernameAvailable(username);
        ParamMap response;
        if (!result) {
            response = new ParamMap().add("success", false).add("message", "该用户不存在");
        } else {
            AppUser user = this.userService.getUserByUsername(username);
            boolean isCourseStudent = this.courseService.isCourseStudent(id, user.getId());
            if (isCourseStudent) {
                response = new ParamMap().add("success", false).add("message", "该用户已是本课程的学员了");
            } else {
                response = new ParamMap().add("success", true).add("message", "");
            }

            boolean isCourseTeacher = this.courseService.isCourseTeacher(id, user.getId());
            if (isCourseTeacher) {
                response = new ParamMap().add("success", false).add("message", "该用户是本课程的教师，不能添加");
            }
        }
        return response.toMap();
    }

    @RequestMapping("/course/{id}/manage/student/export")
    public void exportCsvAction(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        String str = "用户名,Email,加入学习时间,学习进度,姓名,性别,QQ号,微信号,手机号,公司,职业,头衔";
        String filename = String.format("course-%s-students-(%s).csv", id, System.currentTimeMillis());
        response.setHeader("Content-type", "text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setHeader("Content-length", String.valueOf(str.length()));
        try {
            response.getOutputStream().print(str);
        } catch (IOException e) {
            throw new RuntimeGoingException("Export student csv error: " + e.getMessage());
        }
    }

    @RequestMapping("/course/{courseId}/manage/student/{userId}/show")
    public String showAction(@PathVariable Integer courseId, @PathVariable Integer userId, Model model) {
        if (!this.userAccessControlService.hasAnyRole("ROLE_ADMIN", "ROLE_SUPER_ADMIN")) {
            throw new RuntimeGoingException("您无权查看学员详细信息！");
        }

        AppUser user = this.userService.getUser(userId);
        Map<String, Object> profile = this.userService.getUserProfile(userId);
        profile.put("title", user.getTitle());
        model.addAttribute("profile", profile);
        model.addAttribute("user", user);

        List<Map<String, Object>> userFields = this.userFieldService.getAllFieldsOrderBySeqAndEnabled();
        userFields.forEach(e -> {
            this.userFieldService.checkFieldNameSetType(e, "textField", "text");
            this.userFieldService.checkFieldNameSetType(e, "varcharField", "varchar");
            this.userFieldService.checkFieldNameSetType(e, "intField", "int");
            this.userFieldService.checkFieldNameSetType(e, "floatField", "float");
            this.userFieldService.checkFieldNameSetType(e, "dateField", "date");
        });
        model.addAttribute("userFields", userFields);

        return "/course/manage/student/show-modal";
    }

    @RequestMapping("/course/{courseId}/manage/student/{userId}/defined_show")
    public String definedShowAction(@PathVariable Integer courseId, @PathVariable Integer userId, Model model) {
        model.addAttribute("profile", this.userService.getUserProfile(userId));

        List<Map<String, Object>> userFields = this.userFieldService.getAllFieldsOrderBySeqAndEnabled();
        userFields.forEach(e -> {
            this.userFieldService.checkFieldNameSetType(e, "textField", "text");
            this.userFieldService.checkFieldNameSetType(e, "varcharField", "varchar");
            this.userFieldService.checkFieldNameSetType(e, "intField", "int");
            this.userFieldService.checkFieldNameSetType(e, "floatField", "float");
            this.userFieldService.checkFieldNameSetType(e, "dateField", "date");
        });
        model.addAttribute("userFields", userFields);

        Map<String, Object> course = this.settingService.get("course");
        if (EasyStringUtil.isNotBlank(course.get("userinfoFields"))) {
            model.addAttribute("userinfoFields", course.get("userinfoFields"));
        }
        return "/course/manage/student/defined-show-modal";
    }

    @RequestMapping("/course/{courseId}/manage/student/{userId}/remark")
    public String remarkAction(@PathVariable Integer courseId, @PathVariable Integer userId,
                               HttpServletRequest request, Model model) {

        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> course = this.courseService.tryManageCourse(currentUser, courseId);
        AppUser user = this.userService.getUser(userId);
        Map<String, Object> member = this.courseService.getCourseMember(courseId, userId);

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> data = ParamMap.toQueryAllMap(request);
            member = this.courseService.remarkStudent(courseId, userId, String.valueOf(data.get("remark")));
            return this.createStudentTrResponse(course, member, currentUser, model);
        }
        model.addAttribute("default", this.settingService.get("default"));
        model.addAttribute("member", member);
        model.addAttribute("user", user);
        model.addAttribute("course", course);
        return "/course/manage/student/remark-modal";
    }

    private String createStudentTrResponse(Map<String, Object> course, Map<String, Object> student,
                                           AppUser currentUser, Model model) {

        Map<String, Object> courseSetting = this.settingService.get("course");
        model.addAttribute("isTeacherAuthManageStudent",
                EasyStringUtil.isNotBlank(courseSetting.get("teacher_manage_student")) ? 1 : 0);

        Integer studentUserId = ValueParser.toInteger(student.get("userId"));
        model.addAttribute("user", this.userService.getUser(studentUserId));
        model.addAttribute("isFollowing", this.userService.isFollowed(currentUser.getId(), studentUserId));
        model.addAttribute("progress", this.calculateUserLearnProgress(course, student));
        model.addAttribute("default", this.settingService.get("default"));
        model.addAttribute("course", course);
        model.addAttribute("student", student);
        return "/course/manage/student/tr";
    }

    @RequestMapping("/course/{courseId}/manage/student/{userId}/remove")
    @ResponseBody
    public Boolean removeAction(@PathVariable Integer courseId, @PathVariable Integer userId,
                                HttpServletRequest request) {

        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> course;
        Map<String, Object> courseSetting = this.settingService.get("course");
        if (EasyStringUtil.isNotBlank(courseSetting.get("teacher_manage_student"))) {
            course = this.courseService.tryManageCourse(currentUser, courseId);
        } else {
            course = this.courseService.tryAdminCourse(currentUser, courseId);
        }

        this.courseService.removeStudent(currentUser, courseId, userId);

        this.notificationService.notify(userId, "student-remove",
                new ParamMap().add("courseId", course.get("id")).add("courseTitle", course.get("title")).toMap());

        return Boolean.TRUE;
    }
}
