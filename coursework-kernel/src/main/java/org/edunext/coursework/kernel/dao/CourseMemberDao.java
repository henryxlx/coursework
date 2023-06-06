package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseMemberDao {

    void batchDeleteMember(List<Object> ids);

    Map<String, Object> getMemberByCourseIdAndUserId(Integer courseId, Object userId);

    void deleteMember(Object id);

    void addMember(Map<String, Object> member);

    List<Map<String, Object>> findMembersByCourseIdAndRole(Integer courseId, String roleName, Integer start, Integer limit);
}
