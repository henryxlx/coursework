package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.CourseThreadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Controller
public class CourseThreadController {

    private final CourseService courseService;
    private final CourseThreadService threadService;
    private final UserAccessControlService userAccessControlService;
    private final AppUserService userService;

    public CourseThreadController(CourseService courseService,
                                  CourseThreadService threadService,
                                  UserAccessControlService userAccessControlService,
                                  AppUserService userService) {

        this.courseService = courseService;
        this.threadService = threadService;
        this.userAccessControlService = userAccessControlService;
        this.userService = userService;
    }


    @RequestMapping("/course/{id}/thread")
    public ModelAndView indexAction(@PathVariable Integer id, HttpServletRequest request) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        if (!userAccessControlService.isLoggedIn()) {
            return BaseControllerHelper.createMessageResponse("info", "你好像忘了登录哦？",
                    null, 3000, request.getContextPath() + "/login");
        }

        Map<String, Object> course = this.courseService.getCourse(id);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在，或已删除。");
        }

        if (!this.courseService.canTakeCourse(id, currentUser.getId())) {
            return BaseControllerHelper.createMessageResponse("info",
                    String.format("您还不是课程《%s》的学员，请先购买或加入学习。", course.get("title")),
                    null, 3000, request.getContextPath() + "/course/" + id);
        }

        ModelAndView mav = new ModelAndView();

        this.courseService.tryTakeCourse(id, currentUser);

        Map<String, Object> filters = this.getThreadSearchFilters(request);
        Map<String, Object> conditions = this.convertFiltersToConditions(course, filters);

        Paginator paginator = new Paginator(request, this.threadService.searchThreadCount(conditions), 20);

        List<Map<String, Object>> threads = this.threadService.searchThreads(conditions, String.valueOf(filters.get("sort")),
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        mav.addObject("lessons", this.courseService.findLessonsByIds(ArrayToolkit.column(threads, "lessonId")));
        Set<Object> userIds = ArrayToolkit.column(threads, "userId");
        userIds.addAll(ArrayToolkit.column(threads, "latestPostUserId"));
        mav.addObject("users", this.userService.findUsersByIds(userIds));
        mav.addObject("course", course);
        mav.addObject("threads", threads);
        mav.addObject("paginator", paginator);
        mav.addObject("filters", filters);

        String template = "XMLHttpRequest".equals(request.getHeader("x-requested-with")) ? "index-main" : "index";
        mav.setViewName("/course/thread/" + template);
        return mav;
    }

    private Map<String, Object> getThreadSearchFilters(HttpServletRequest request) {
        Map<String, Object> filters = new HashMap<>(2);
        filters.put("type", request.getParameter("type"));
        if (!ArrayUtil.inArray(filters.get("type"), "all", "question", "elite")) {
            filters.put("type", "all");
        }
        filters.put("sort", request.getParameter("sort"));

        if (!ArrayUtil.inArray(filters.get("sort"), "created", "posted", "createdNotStick", "postedNotStick")) {
            filters.put("sort", "posted");
        }
        return filters;
    }

    private Map<String, Object> convertFiltersToConditions(Map<String, Object> course, Map<String, Object> filters) {
        Map<String, Object> conditions = new HashMap<>(2);
        conditions.put("courseId", course.get("id"));
        String type = String.valueOf(filters.get("type"));
        switch (type) {
            case "question":
                conditions.put("type", "question");
                break;
            case "elite":
                conditions.put("isElite", 1);
                break;
            default:
                break;
        }
        return conditions;
    }

    @RequestMapping("/course/{id}/thread/create")
    public String createAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        AppUser currentUser = AppUser.getCurrentUser(request);
        Map<String, Object> course = this.courseService.getCourse(id);
        Map<String, Object> member = this.courseService.tryTakeCourse(id, currentUser);

        if (MapUtil.isNotEmpty(member) && !this.courseService.isMemberNonExpired(course, member)) {
            return "redirect:/course/" + id + "/thread";
        }

        if (MapUtil.isNotEmpty(member) && ValueParser.parseInt(member.get("levelId")) > 0) {
//            if ( !"ok".equals(this.vipService.checkUserInMemberLevel(member.get("userId"), course.get("vipLevelId")))) {
//                return "redirect:/course/" + id;
//            }
        }


        String type = ServletRequestUtils.getStringParameter(request, "type", "discussion");
        if ("POST".equals(request.getMethod())) {
            Map<String, Object> data = ParamMap.toFormDataMap(request);
            data.put("title", data.get("thread[title]"));
            data.put("content", data.get("thread[content]"));
            data.remove("thread[title]");
            data.remove("thread[content]");
            Map<String, Object> thread = this.threadService.createThread(data, currentUser);
            return "redirect:/course/" + id + "/thread/" + thread.get("id");
        }

        model.addAttribute("course", course);
        model.addAttribute("courseId", id);
        model.addAttribute("type", type);
        return "/course/thread/form";
    }

    @RequestMapping("/course/{courseId}/thread/{id}")
    public ModelAndView showAction(@PathVariable Integer courseId, @PathVariable Integer id,
                                   HttpServletRequest request) {

        AppUser currentUser = AppUser.getCurrentUser(request);
        if (!userAccessControlService.isLoggedIn()) {
            return BaseControllerHelper.createMessageResponse("info", "你好像忘了登录哦？",
                    null, 3000, request.getContextPath() + "/login");
        }

        Map<String, Object> course = this.courseService.getCourse(courseId);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在，或已删除。");
        }

        if (!this.courseService.canTakeCourse(id, currentUser.getId())) {
            return BaseControllerHelper.createMessageResponse("info",
                    String.format("您还不是课程《%s》的学员，请先购买或加入学习。", course.get("title")),
                    null, 3000, request.getContextPath() + "/course/" + id);
        }

        ModelAndView mav = new ModelAndView("/course/thread/show");

        Map<String, Object> member = this.courseService.tryTakeCourse(courseId, currentUser);
        boolean isMemberNonExpired = true;
        if (MapUtil.isNotEmpty(member) && !this.courseService.isMemberNonExpired(course, member)) {
            // return $this->redirect($this->generateUrl('course_threads',array('id' => $courseId)));
            isMemberNonExpired = false;
        }
        mav.addObject("isMemberNonExpired", isMemberNonExpired);

        Map<String, Object> thread = this.threadService.getThread(courseId, id);
        if (MapUtil.isEmpty(thread)) {
            throw new RuntimeGoingException("话题不存在，或已删除。");
        }

        Paginator paginator = new Paginator(request, this.threadService.getThreadPostCount(courseId, id), 30);

        List<Map<String, Object>> posts = this.threadService.findThreadPosts(courseId, id, "default",
                paginator.getOffsetCount(),
                paginator.getPerPageCount());

        if ("question".equals(thread.get("type")) && paginator.getCurrentPage() == 1) {
            mav.addObject("elitePosts", this.threadService.findThreadElitePosts(courseId, id, 0, 10));
        }

        mav.addObject("users", this.userService.findUsersByIds(ArrayToolkit.column(posts, "userId")));

        this.threadService.hitThread(courseId, id);

        mav.addObject("isManager", this.courseService.canManageCourse(courseId, currentUser.getId()));
        mav.addObject("course", course);

        mav.addObject("lesson", this.courseService.getCourseLesson(courseId,
                ValueParser.toInteger(thread.get("lessonId"))));
        mav.addObject("thread", thread);
        mav.addObject("author", this.userService.getUser(thread.get("userId")));
        mav.addObject("posts", posts);
        mav.addObject("paginator", paginator);
        return mav;
    }
}
