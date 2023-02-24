package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.AppUserDao;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppLogService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class CourseExtendUserServiceImpl {

    private final AppUserDao userDao;

    private final AppLogService logService;

    public CourseExtendUserServiceImpl(AppUserDao userDao, AppLogService logService) {
        this.userDao = userDao;
        this.logService = logService;
    }

    public void promoteUser(AppUser currentUser, Integer userId) {
        AppUser user = userDao.getUser(userId);
        if (user == null) {
            throw new RuntimeGoingException("用户不存在，推荐失败！");
        }

        Map<String, Object> fields = new ParamMap().add("id", user.getId())
                .add("promoted", 1).add("promotedTime", System.currentTimeMillis()).toMap();
        int nums = userDao.updateMap(fields);
        if (nums > 0) {
            logService.info(currentUser, "user", "recommend",
                    String.format("推荐用户%s(#%d)", user.getUsername(), user.getId()));
        }
    }

    public void cancelPromoteUser(AppUser currentUser, Integer userId) {
        AppUser user = userDao.getUser(userId);
        if (user == null) {
            throw new RuntimeGoingException("用户不存在，取消推荐失败！");
        }

        Map<String, Object> fields = new ParamMap().add("id", user.getId())
                .add("promoted", 0).add("promotedTime", 0).toMap();
        int nums = userDao.updateMap(fields);
        if (nums > 0) {
            logService.info(currentUser, "user", "cancel_recommend",
                    String.format("取消推荐用户%s(#%d)", user.getUsername(), user.getId()));
        }
    }
}
