package org.edunext.coursework.web.controller;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Controller
public class MyCourseController {

    private final CourseService courseService;
    private final AppUserService userService;

    public MyCourseController(CourseService courseService, AppUserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @RequestMapping("/my/courses/learning")
    public String learningAction(HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Paginator paginator = new Paginator(request,
                courseService.findUserLeaningCourseCount(currentUser.getId(), null),
                12);

        model.addAttribute("courses", courseService.findUserLeaningCourses(currentUser.getId(),
                paginator.getOffsetCount(), paginator.getPerPageCount(), null));

        model.addAttribute("paginator", paginator);

        return "/my/course/learning";
    }

    @RequestMapping("/my/courses/learned")
    public String learnedAction(HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Paginator paginator = new Paginator(request,
                this.courseService.findUserLearnedCourseCount(currentUser.getId()), 12);

        List<Map<String, Object>> courses = this.courseService.findUserLearnedCourses(currentUser.getId(),
                paginator.getOffsetCount(), paginator.getPerPageCount());

        Set<Object> userIds = new HashSet<>();
        courses.forEach(course -> {
            this.courseService.mergeTeacherIds(userIds, course.get("teacherIds"));
            int learnTime = this.courseService.searchLearnTime(new ParamMap()
                    .add("courseId", course.get("id")).add("userId", currentUser.getId()).toMap());

            course.put("learnTime", (learnTime / 60) + "小时" + (learnTime % 60) + "分钟");
        });
        model.addAttribute("users", this.userService.findUsersByIds(userIds));

        model.addAttribute("courses", courses);
        model.addAttribute("paginator", paginator);
        return "/my/course/learned";
    }

    @RequestMapping("/my/courses/favorited")
    public String favoritedAction(HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Paginator paginator = new Paginator(request,
                this.courseService.findUserFavoritedCourseCount(currentUser.getId()), 12);

        List<Map<String, Object>> courses = this.courseService.findUserFavoritedCourses(currentUser.getId(),
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        Set<Object> userIds = new HashSet<>();
        courses.forEach(favoriteCourse -> {
            this.courseService.mergeTeacherIds(userIds, favoriteCourse.get("teacherIds"));
        });
        model.addAttribute("users", this.userService.findUsersByIds(userIds));
        model.addAttribute("courses", courses);
        model.addAttribute("paginator", paginator);
        return "/my/course/favorited";
    }
}
