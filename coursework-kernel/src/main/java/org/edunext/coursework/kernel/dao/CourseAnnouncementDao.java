package org.edunext.coursework.kernel.dao;

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
}
