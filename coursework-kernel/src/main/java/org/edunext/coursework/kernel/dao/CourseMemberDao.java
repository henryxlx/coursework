package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseMemberDao {

    Map<String, Object> getMember(Object id);

    void batchDeleteMember(List<Object> ids);

    Map<String, Object> getMemberByCourseIdAndUserId(Integer courseId, Object userId);

    void deleteMember(Object id);

    void addMember(Map<String, Object> member);

    List<Map<String, Object>> findMembersByCourseIdAndRole(Integer courseId, String roleName, Integer start, Integer limit);

    Integer searchMemberCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchMembers(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    List<Map<String, Object>> searchMember(Map<String, Object> conditions, Integer start, Integer limit);

    Integer findMemberCountByUserIdAndRole(Integer userId, String role, boolean onlyPublished);

    Integer findMemberCountByUserIdAndCourseTypeAndIsLearned(Integer userId, String role, String type, Integer isLearned);

    Integer findMemberCountByUserIdAndRoleAndIsLearned(Integer userId, String role, Integer isLearned);

    List<Map<String, Object>> findMembersByUserIdAndRole(Integer userId, String role,
                                                         Integer start, Integer limit, boolean onlyPublished);

    List<Map<String, Object>> findMembersByUserIdAndCourseTypeAndIsLearned(Integer userId, String role, String type,
                                                                           Integer isLearned, Integer start, Integer limit);

    List<Map<String, Object>> findMembersByUserIdAndRoleAndIsLearned(Integer userId, String role,
                                                                     Integer isLearned, Integer start, Integer limit);

    void updateMember(Object id, Map<String, Object> member);

    Integer findMemberCountByCourseIdAndRole(Integer courseId, String role);
}
