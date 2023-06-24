package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.EasyWebFormEditor;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.CourseService;
import org.edunext.coursework.kernel.service.UploadFileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xulixin
 */
@Controller
public class UploadFileController {

    private final UserAccessControlService userAccessControlService;
    private final UploadFileService uploadFileService;
    private final CourseService courseService;

    public UploadFileController(UserAccessControlService userAccessControlService,
                                UploadFileService uploadFileService,
                                CourseService courseService) {

        this.userAccessControlService = userAccessControlService;
        this.uploadFileService = uploadFileService;
        this.courseService = courseService;
    }

    @RequestMapping("/uploadfile/upload")
    @ResponseBody
    public Map<String, Object> uploadAction(MultipartFile file, String[] targetType, Integer targetId,
                                            HttpServletRequest request) {

//        $token = $request->request->get('token');
//        $token = $this->getUserService()->getToken('fileupload', $token);
//        if (empty($token)) {
//            throw $this->createAccessDeniedException('上传TOKEN已过期或不存在。');
//        }
//
//        $user = $this->getUserService()->getUser($token['userId']);
//        if (empty($user)) {
//            throw $this->createAccessDeniedException('上传TOKEN非法。');
//        }

        return this.uploadFileService.addFile(targetType[0], targetId,
                AppUser.putCurrentUser(new HashMap<>(1), request), "local", file);
    }

    @RequestMapping("/uploadfile/browser")
    @ResponseBody
    public List<Map<String, Object>> browserAction(HttpServletRequest request) {
        AppUser user = AppUser.getCurrentUser(request);
        if (!userAccessControlService.hasAnyRole("ROLE_TEACHER", "ROLE_ADMIN")) {
            throw new RuntimeGoingException("您无权查看此页面！");
        }

        Map<String, Object> conditions = ParamMap.toQueryAllMap(request);
        conditions.put("currentUserId", user.getId());

        return this.uploadFileService.searchFiles(conditions, "latestUpdated", 0, 10000);
    }

    @RequestMapping("/uploadfile/browsers")
    @ResponseBody
    public List<Map<String, Object>> browsersAction(HttpServletRequest request) {
        if (!userAccessControlService.hasRole("ROLE_TEACHER") && !userAccessControlService.isAdmin()) {
            throw new RuntimeGoingException("您无权查看此页面！");
        }

        Map<String, Object> conditions = ParamMap.toQueryAllMap(request);
        return this.uploadFileService.searchFiles(conditions, "latestUpdated", 0, 10000);
    }

    @RequestMapping("/uploadfile/params")
    @ResponseBody
    public Map<String, Object> paramsAction(HttpServletRequest request) {
        AppUser user = AppUser.getCurrentUser(request);
        if (!userAccessControlService.isLoggedIn()) {
            throw new RuntimeGoingException("未登录不允许执行此操作！");
        }

        Map<String, Object> params = EasyWebFormEditor.createFormEditor().bind(request).getData();

        params.put("user", user.getId());
        params.put("defaultUploadUrl", BaseControllerHelper.generateUrl(request.getContextPath() + "/uploadfile/upload",
                FastHashMap.build(2).add("targetType", params.get("targetType"))
                        .add("targetId", EasyStringUtil.isNotBlank(params.get("targetId")) ? params.get("targetId") : 0).toMap()));

        if (EasyStringUtil.isBlank(params.get("lazyConvert"))) {
            params.put("convertCallback", request.getContextPath() + "/uploadfile/cloud_convertcallback2");
        } else {
            params.put("convertCallback", null);
        }

        return this.uploadFileService.makeUploadParams(params);
    }

    @RequestMapping("/course/{id}/delete/files/{type}")
    @ResponseBody
    public Boolean deleteCourseFilesAction(@PathVariable Integer id, @PathVariable String type,
                                           HttpServletRequest request) {

        if (id != null) {
            this.courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        }

        String[] ids = request.getParameterValues("ids[]");
        Set<Integer> fileIds = Arrays.stream(ids).map(ValueParser::toInteger).collect(Collectors.toSet());
        this.uploadFileService.deleteFiles(fileIds);

        return Boolean.TRUE;
    }
}
