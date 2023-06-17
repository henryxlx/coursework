package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.mvc.block.BlockRenderController;
import com.jetwinner.webfast.mvc.block.BlockRenderMethod;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.CourseThreadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author xulixin
 */
@Controller("frontCourseBlockController")
public class CourseBlockController implements BlockRenderController {

    private final CourseService courseService;
    private final CourseThreadService threadService;
    private final UserAccessControlService userAccessControlService;
    private final AppSettingService settingService;
    private final AppUserService userService;

    public CourseBlockController(CourseService courseService,
                                 CourseThreadService threadService,
                                 UserAccessControlService userAccessControlService,
                                 AppSettingService settingService,
                                 AppUserService userService) {

        this.courseService = courseService;
        this.threadService = threadService;
        this.userAccessControlService = userAccessControlService;
        this.settingService = settingService;
        this.userService = userService;
    }

    @RequestMapping("/course/header")
    @BlockRenderMethod
    @SuppressWarnings("unchecked")
    public String headerBlockAction(HttpServletRequest request,
                                    Map<String, Object> course,
                                    Integer courseId,
                                    Boolean manage,
                                    Model model) {

        AppUser user = AppUser.getCurrentUser(request);
        course = (Map<String, Object>) request.getAttribute("course");

        Map<String, Object> member = this.courseService.getCourseMember(courseId, user.getId());
        Map<String, AppUser> users = MapUtil.newHashMap(0);
        if (MapUtil.isNotEmpty(course)) {
            Set<Object> teacherIds = this.courseService.getTeacherIds(course.get("teacherIds"));
            users = this.userService.findUsersByIds(teacherIds);
        }
        model.addAttribute("usersForHeader", users);

        if (MapUtil.isEmpty(member)) {
            member.put("deadline", 0);
            member.put("levelId", 0);
        }

        String vipChecked = "ok";
        if (ValueParser.parseInt(member.get("levelId")) > 0) {
//            vipChecked = this.vipService.checkUserInMemberLevel(user.get("id"), course.get("vipLevelId"));
        }

        Boolean canExit = false;
//        List<Map<String, Object>> classroomMembers = this.getClassroomMembersByCourseId(courseId);
        List<Map<String, Object>> classroomMembers = new ArrayList<>(0);
        Set<Object> classroomMemberRoles = ArrayToolkit.column(classroomMembers, "role");
        if ("student".equals(member.get("role")) && "course".equals(member.get("joinedType"))
                || ("classroom".equals(member.get("joinedType")) && (classroomMemberRoles == null || classroomMemberRoles.size() == 0))) {
            canExit = true;
        }
//        model.addAttribute("course", course);
        model.addAttribute("canManage", this.courseService.canManageCourse(courseId, user.getId()));
        model.addAttribute("canExit", canExit);
        model.addAttribute("member", member);
        model.addAttribute("manage", manage);
//        model.addAttribute("isNonExpired", this.courseService.isMemberNonExpired(course, member));
        model.addAttribute("vipChecked", vipChecked);
        model.addAttribute("isAdmin", userAccessControlService.hasRole("ROLE_SUPER_ADMIN"));
        return "/course/header";
    }

    @RequestMapping("/course/coursesBlock")
    @BlockRenderMethod
    @SuppressWarnings("unchecked")
    public String coursesBlockAction(HttpServletRequest request,
                                     @RequestParam(defaultValue = "list") String view,
                                     @RequestParam(defaultValue = "default") String mode,
                                     Model model) {

        // 此处强制类型转换需要使用注解@SuppressWarnings压制异常检查
        List<Map<String, Object>> courses = (List<Map<String, Object>>) request.getAttribute("courses");
        Set<Object> courseIds = new HashSet<>();
        Set<Object> userIds = new HashSet<>();
        courses.forEach(x -> {
            String[] teacherIds = EasyStringUtil.explode(", ", x.get("teacherIds"));
            x.put("teacherIds", teacherIds);
            courseIds.add(x.get("id"));
            userIds.add(teacherIds);
        });

        if ("true".equals(settingService.getSettingValue("classroom.enabled"))) {
//            List<Classroom> classrooms = classroomService.findClassroomsByCourseIds(courseIds);
//            $classroomIds = ArrayToolkit.column($classrooms,'classroomId');
//            $courses[$key]['classroomCount']=count($classroomIds);
//            if(count($classroomIds)>0) {
//                $courses[$key]['classroom'] = classroomService.getClassroom(classroomIds[0]);
//            }
//            model.addAttribute("classrooms", classrooms);
        }

        model.addAttribute("courses", courses);
        model.addAttribute("users", userService.findUsersByIds(userIds));
        model.addAttribute("mode", mode);
        return "/course/courses-block-" + view;
    }

    @RequestMapping("/course/teachersBlock")
    @BlockRenderMethod
    @SuppressWarnings("unchecked")
    public String teachersBlockAction(HttpServletRequest request,
                                      Model model) {

        List<Object> teacherIds = (List<Object>) request.getAttribute("teacherIds");
        Set<Object> ids = courseService.getTeacherIds(teacherIds);
        model.addAttribute("users", this.userService.findUsersByIds(ids));
        model.addAttribute("profiles", ArrayToolkit.index(this.userService.findUserProfilesByIds(ids), "id"));

        return "/course/teachers-block";
    }

    @RequestMapping("/course/latestMembersBlock")
    @BlockRenderMethod
    public String latestMembersBlockAction(Integer courseId, Model model) {
        List<Map<String, Object>> students = this.courseService.findCourseStudents(courseId, 0, 12);
        model.addAttribute("users", this.userService.findUsersByIds(ArrayToolkit.column(students, "userId")));
        model.addAttribute("students", students);
        return "/course/latest-members-block";
    }

    @RequestMapping("/course/thread/latestBlock")
    @BlockRenderMethod
    public String latestThreadBlockAction(Integer courseId, Model model) {
        model.addAttribute("threads",
                this.threadService.searchThreads(new ParamMap().add("courseId", courseId).toMap(),
                        "createdNotStick", 0, 10));

        return "course/thread/latest-block";
    }

    @RequestMapping("/course/announcementBlock")
    @BlockRenderMethod
    public String announcementBlockAction(Integer courseId, Model model) {
//        model.addAttribute("announcements", this.courseService.findAnnouncements(courseId, 0, 10));
//        model.addAttribute("canManage", this.courseService.canManageCourse(courseId));
//        model.addAttribute("canTake", this.courseService.canTakeCourse(course));
        return "/course/announcement-block";
    }

    @RequestMapping("/course/progressBlock")
    @BlockRenderMethod
    @SuppressWarnings("unchecked")
    public String progressBlockAction(Map<String, Object> course, Integer courseId, Integer userId,
                                      HttpServletRequest request,
                                      Model model) {

        course = (Map<String, Object>) request.getAttribute("course");
        Map<String, Object> member = this.courseService.getCourseMember(courseId, userId);
        model.addAttribute("nextLearnLesson", this.courseService.getUserNextLearnLesson(userId, courseId));

        model.addAttribute("progress", this.calculateUserLearnProgress(course, member));
        model.addAttribute("member", member);
        return "/course/progress-block";
    }

    private Map<String, Object> calculateUserLearnProgress(Map<String, Object> course, Map<String, Object> member) {
        Map<String, Object> map = new HashMap<>(3);
        if (ValueParser.parseInt(course.get("lessonNum")) == 0) {
            map.put("percent", "0%");
            map.put("number", 0);
            map.put("total", 0);
            return map;
        }

        int learnedNum = ValueParser.parseInt(member.get("learnedNum"));
        int lessonNum = ValueParser.parseInt(course.get("lessonNum"));
        int percent = (int) (1.0 * learnedNum / lessonNum * 100);
        map.put("percent", percent + "%");
        map.put("number", member.get("learnedNum"));
        map.put("total", course.get("lessonNum"));
        return map;
    }

    @GetMapping("/course/threadPostBlock")
    @BlockRenderMethod
    public String threadPostBlockAction(Integer courseId, Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> thread = this.threadService.getThread(courseId, id);
        model.addAttribute("thread", thread);
        model.addAttribute("courseId", thread.get("courseId"));
        model.addAttribute("threadId", id);
        return "/course/thread/post";
    }
}
