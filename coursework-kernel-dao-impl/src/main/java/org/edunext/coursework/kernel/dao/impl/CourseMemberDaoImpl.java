package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.CourseMemberDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class CourseMemberDaoImpl extends FastJdbcDaoSupport implements CourseMemberDao {

    private static final String TABLE_NAME = "cw_course_member";

    @Override
    public void batchDeleteMember(List<Object> ids) {

    }

    @Override
    public Map<String, Object> getMemberByCourseIdAndUserId(Integer courseId, Object userId) {
        return null;
    }

    @Override
    public void deleteMember(Object id) {

    }

    @Override
    public void addMember(Map<String, Object> member) {

    }

    @Override
    public List<Map<String, Object>> findMembersByCourseIdAndRole(Integer courseId, String roleName,
                                                                  Integer start, Integer limit) {

        return new ArrayList<>(0);
    }

    @Override
    public Integer searchMemberCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions).select("COUNT(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchMember(Map<String, Object> conditions, Integer start, Integer limit) {
        return new ArrayList<>(0);
    }

    @Override
    public Integer findMemberCountByUserIdAndRole(Integer userId, String role, boolean onlyPublished) {
        return 0;
    }

    @Override
    public Integer findMemberCountByUserIdAndCourseTypeAndIsLearned(Integer userId, String role, String type, Integer isLearned) {
        return 0;
    }

    @Override
    public Integer findMemberCountByUserIdAndRoleAndIsLearned(Integer userId, String role, Integer isLearned) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findMembersByUserIdAndRole(Integer userId, String role, Integer start, Integer limit, boolean onlyPublished) {
        return new ArrayList<>(0);
    }

    @Override
    public List<Map<String, Object>> findMembersByUserIdAndCourseTypeAndIsLearned(Integer userId, String role, String type, String isLearned, Integer start, Integer limit) {
        return new ArrayList<>(0);
    }

    @Override
    public List<Map<String, Object>> findMembersByUserIdAndRoleAndIsLearned(Integer userId, String role, String isLearned, Integer start, Integer limit) {
        return new ArrayList<>(0);
    }

    private DynamicQueryBuilder createSearchQueryBuilder(Map<String, Object> conditions) {
        return new DynamicQueryBuilder(conditions)
                .from(TABLE_NAME, "course_member")
                .andWhere("userId = :userId")
                .andWhere("courseId = :courseId")
                .andWhere("isLearned = :isLearned")
                .andWhere("noteNum > :noteNumGreaterThan")
                .andWhere("role = :role")
                .andWhere("createdTime >= :startTimeGreaterThan")
                .andWhere("createdTime < :startTimeLessThan")
                .andWhere("courseId IN (:courseIds)");
    }
}
