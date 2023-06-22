package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.AppUser;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseAnnouncementService {

    Integer searchAnnouncementCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchAnnouncements(Map<String, Object> conditions, String sort, Integer start, Integer limit);

    void deleteAnnouncement(Integer id, AppUser currentUser);

    void deleteAnnouncement(String[] ids, AppUser currentUser);
}
