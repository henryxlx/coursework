package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.util.ListUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Controller
public class UploadFileController {

    private final UserAccessControlService accessControlService;

    public UploadFileController(UserAccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @RequestMapping("/uploadfile/browser")
    @ResponseBody
    public List<Map<String, Object>> browserAction(HttpServletRequest request) {
        AppUser user = AppUser.getCurrentUser(request);
        if (!accessControlService.hasAnyRole("ROLE_TEACHER", "ROLE_ADMIN")) {
            throw new RuntimeGoingException("您无权查看此页面！");
        }

        Map<String, Object> conditions = ParamMap.toQueryAllMap(request);
        conditions.put("currentUserId", user.getId());

//        List<Map<String, Object>> files = this.uploadFileService.searchFiles(conditions, "latestUpdated", 0, 10000);
        return ListUtil.newArrayList();
    }
}
