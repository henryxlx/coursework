package org.edunext.coursework.kernel.service;

import com.jetwinner.util.ArrayUtil;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppLogService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import org.edunext.coursework.kernel.dao.CourseAnnouncementDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class CourseAnnouncementServiceImpl implements CourseAnnouncementService {

    private final CourseAnnouncementDao announcementDao;
    private final CourseService courseService;
    private final AppUserService userService;
    private final AppLogService logService;

    public CourseAnnouncementServiceImpl(CourseAnnouncementDao announcementDao,
                                         CourseService courseService,
                                         AppUserService userService,
                                         AppLogService logService) {

        this.announcementDao = announcementDao;
        this.courseService = courseService;
        this.userService = userService;
        this.logService = logService;
    }

    @Override
    public Integer searchAnnouncementCount(Map<String, Object> conditions) {
        conditions = this.prepareSearchConditions(conditions);
        return this.announcementDao.searchAnnouncementCount(conditions);
    }

    @Override
    public List<Map<String, Object>> searchAnnouncements(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        OrderBy orderBy;
        switch (sort) {
            case "created":
                orderBy = OrderBy.build(1).addDesc("createdTime");
                break;
            case "updated":
                orderBy = OrderBy.build(1).addDesc("updatedTime");
                break;
            default:
                throw new RuntimeGoingException("参数sort不正确。");
        }

        conditions = this.prepareSearchConditions(conditions);
        return this.announcementDao.searchAnnouncements(conditions, orderBy, start, limit);
    }

    @Override
    public void deleteAnnouncement(Integer id, AppUser currentUser) {
        Map<String, Object> announcement = this.announcementDao.getAnnouncement(id);
        if (MapUtil.isEmpty(announcement)) {
            throw new RuntimeGoingException("公告(#" + id + ")不存在，删除失败");
        }

        int announcementUserId = ValueParser.parseInt(announcement.get("userId"));
        if ((announcementUserId != currentUser.getId()) &&
                !this.courseService.canManageCourse(ValueParser.toInteger(announcement.get("courseId")), currentUser.getId())) {

            throw new RuntimeGoingException("你没有权限删除公告(#" + id + ")");
        }

        this.announcementDao.deleteAnnouncement(id);

        if (announcementUserId != currentUser.getId()) {
            this.logService.info(currentUser, "announcement", "delete", "删除公告#" + id);
        }
    }

    @Override
    public void deleteAnnouncement(String[] ids, AppUser currentUser) {
        for (String id : ids) {
            this.deleteAnnouncement(ValueParser.toInteger(id), currentUser);
        }
    }

    private Map<String, Object> prepareSearchConditions(Map<String, Object> conditions) {

        if (EasyStringUtil.isNotBlank(conditions.get("keywordType")) &&
                EasyStringUtil.isNotBlank(conditions.get("keyword"))) {

            if (!ArrayUtil.inArray(conditions.get("keywordType"), "content", "courseId", "courseTitle")) {
                throw new RuntimeGoingException("keywordType参数不正确");
            }
            conditions.put(String.valueOf(conditions.get("keywordType")), conditions.get("keyword"));
        }
        conditions.remove("keywordType");
        conditions.remove("keyword");

        if (EasyStringUtil.isNotBlank(conditions.get("author"))) {
            AppUser author = this.userService.getUserByUsername(String.valueOf(conditions.get("author")));
            conditions.put("userId", author != null ? author.getId() : -1);
            conditions.remove("author");
        }

        return conditions;
    }
}
