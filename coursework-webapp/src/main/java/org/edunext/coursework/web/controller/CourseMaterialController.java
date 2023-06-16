package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseMaterialService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseMaterialController {

    private final CourseService courseService;
    private final CourseMaterialService materialService;
    private final UserAccessControlService userAccessControlService;
    private final AppUserService userService;

    public CourseMaterialController(CourseService courseService,
                                    CourseMaterialService materialService,
                                    UserAccessControlService userAccessControlService,
                                    AppUserService userService) {

        this.courseService = courseService;
        this.materialService = materialService;
        this.userAccessControlService = userAccessControlService;
        this.userService = userService;
    }


    @RequestMapping("/course/{id}/material")
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

        ModelAndView mav = new ModelAndView("/course/material/index");

        this.courseService.tryTakeCourse(id, currentUser);

        Paginator paginator = new Paginator(request, this.materialService.getMaterialCount(id), 20);

        mav.addObject("materials",
                this.materialService.findCourseMaterials(id, paginator.getOffsetCount(), paginator.getPerPageCount()));


        List<Map<String, Object>> lessons = this.courseService.getCourseLessons(id);
        mav.addObject("lessons", ArrayToolkit.index(lessons, "id"));

        mav.addObject("course", course);
        mav.addObject("paginator", paginator);
        return mav;
    }
}
