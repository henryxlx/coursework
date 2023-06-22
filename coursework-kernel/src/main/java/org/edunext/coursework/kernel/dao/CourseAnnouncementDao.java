package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CourseAnnouncementDao {

    Map<String, Object> getAnnouncement(Object id);

    List<Map<String, Object>> findAnnouncementsByCourseId(Object courseId, Integer start, Integer limit);

    List<Map<String, Object>> findAnnouncementsByCourseIds(Set<Object> ids, Integer start, Integer limit);

    Map<String, Object> addAnnouncement(Map<String, Object> fields);

    int deleteAnnouncement(Object id);

    int updateAnnouncement(Object id, Map<String, Object> fields);

    Integer searchAnnouncementCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchAnnouncements(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);
}
