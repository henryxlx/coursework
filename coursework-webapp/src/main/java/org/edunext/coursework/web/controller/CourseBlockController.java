package org.edunext.coursework.web.controller;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.SetUtil;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.mvc.block.BlockRenderController;
import com.jetwinner.webfast.mvc.block.BlockRenderMethod;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.ThreadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Controller("frontCourseBlockController")
public class CourseBlockController implements BlockRenderController {

    private final CourseService courseService;
    private final ThreadService threadService;
    private final AppSettingService settingService;
    private final AppUserService userService;

    public CourseBlockController(CourseService courseService,
                                 ThreadService threadService,
                                 AppSettingService settingService,
                                 AppUserService userService) {

        this.courseService = courseService;
        this.threadService = threadService;
        this.settingService = settingService;
        this.userService = userService;
    }

    @RequestMapping("/course/header")
    @BlockRenderMethod
    public String headerBlockAction() {
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
    public String teachersBlockAction(Map<String, Object> course, Model model) {
        Object ids = course.get("teacherIds");
        Set<Object> teacherIds = SetUtil.newHashSet();
        model.addAttribute("users", this.userService.findUsersByIds(teacherIds));
        model.addAttribute("profiles", this.userService.findUserProfilesByIds(teacherIds));

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

    @RequestMapping("/courseThread/latestBlock")
    @BlockRenderMethod
    public String latestThreadBlockAction(Integer courseId, Model model) {
        model.addAttribute("threads",
                this.threadService.searchThreads(new ParamMap().add("courseId", courseId).toMap(),
                        "createdNotStick", 0, 10));

        return "course/thread/latest-block";
    }
}
