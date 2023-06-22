package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.EasyWebFormEditor;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.mvc.BaseControllerHelper;
import org.edunext.coursework.kernel.service.UploadFileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class UploadFileController {

    private final UserAccessControlService userAccessControlService;
    private final UploadFileService uploadFileService;

    public UploadFileController(UserAccessControlService userAccessControlService,
                                UploadFileService uploadFileService) {

        this.userAccessControlService = userAccessControlService;
        this.uploadFileService = uploadFileService;
    }

    @RequestMapping("/uploadfile/upload")
    @ResponseBody
    public Map<String, Object> uploadAction(MultipartFile file, String[] targetType, Integer targetId, AppUser appUser) {
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

        return this.uploadFileService.getImpl().addFile(targetType[0], targetId,
                AppUser.putCurrentUser(new HashMap<>(1), appUser), file);
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

        List<Map<String, Object>> files = this.uploadFileService.searchFiles(conditions, "latestUpdated", 0, 10000);
        return files;
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

        params.put("storage", "local");
        params.put("url", params.get("defaultUploadUrl"));
        return this.uploadFileService.makeUploadParams(params);
    }
}
