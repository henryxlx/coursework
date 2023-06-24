package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseMaterialService;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.UploadFileService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class CourseMaterialController {

    private final CourseService courseService;
    private final CourseMaterialService materialService;
    private final UploadFileService uploadFileService;
    private final UserAccessControlService userAccessControlService;
    private final AppUserService userService;

    public CourseMaterialController(CourseService courseService,
                                    CourseMaterialService materialService,
                                    UploadFileService uploadFileService,
                                    UserAccessControlService userAccessControlService,
                                    AppUserService userService) {

        this.courseService = courseService;
        this.materialService = materialService;
        this.uploadFileService = uploadFileService;
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

    @RequestMapping("/course/{courseId}/material/{materialId}/download")
    public String downloadAction(@PathVariable Integer courseId, @PathVariable Integer materialId,
                                 HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> member = this.courseService.tryTakeCourse(courseId, AppUser.getCurrentUser(request));
        Map<String, Object> course = this.courseService.getCourse(courseId);

        if (MapUtil.isNotEmpty(member) && !this.courseService.isMemberNonExpired(course, member)) {
            return "redirect:/course/" + courseId + "materials";
        }

        if (ValueParser.parseInt(member.get("levelId")) > 0) {
//            if (this.vipService.checkUserInMemberLevel(member.get("userId"), !"ok".equals(course.get("vipLevelId")))) {
//                return "redirect:/course/"+ courseId + "/show";
//            }
        }

        Map<String, Object> material = this.materialService.getMaterial(courseId, materialId);
        if (MapUtil.isEmpty(material)) {
            throw new RuntimeGoingException("课程资料不存在！");
        }

        Map<String, Object> file = this.uploadFileService.getFile(material.get("fileId"));
        if (MapUtil.isEmpty(file)) {
            throw new RuntimeGoingException("课程资料上传的文件不存在！");
        }

        if ("cloud".equals(file.get("storage"))) {
        } else {
            this.createPrivateFileDownloadResponse(response, file);
        }
        return "";
    }

    private void createPrivateFileDownloadResponse(HttpServletResponse response,
                                                   Map<String, Object> file) {

        String fileName = String.valueOf(file.get("filename"));
        try {
            InputStream is = new FileInputStream(String.valueOf(file.get("fullpath")));
            OutputStream os = response.getOutputStream();
            byte[] bytes = StreamUtils.copyToByteArray(is);
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.addHeader("Content-Length", "" + bytes.length);
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeGoingException(String.format("Download file(%s) error: %s", fileName, e.getMessage()));
        }

    }
}
