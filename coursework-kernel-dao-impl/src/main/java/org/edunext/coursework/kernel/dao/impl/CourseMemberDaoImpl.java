package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.CourseMemberDao;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class CourseMemberDaoImpl extends FastJdbcDaoSupport implements CourseMemberDao {

    private static final String TABLE_NAME = "cw_course_member";

    @Override
    public Map<String, Object> getMember(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
    }

    @Override
    public void batchDeleteMember(List<Object> ids) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, ValueParser.parseInt(ids.get(i)));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    @Override
    public Map<String, Object> getMemberByCourseIdAndUserId(Integer courseId, Object userId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE userId = ? AND courseId = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, userId, courseId).stream().findFirst().orElse(MapUtil.newHashMap(0));
    }

    @Override
    public void deleteMember(Object id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        getJdbcTemplate().update(sql, id);
    }

    @Override
    public void addMember(Map<String, Object> member) {
        int id = insertMapReturnKey(TABLE_NAME, member).intValue();
        member.put("id", id);
    }

    @Override
    public List<Map<String, Object>> findMembersByCourseIdAndRole(Integer courseId, String roleName,
                                                                  Integer start, Integer limit) {

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE courseId = ? AND role = ? ORDER BY seq,createdTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, courseId, roleName);
    }

    @Override
    public Integer searchMemberCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions).select("COUNT(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchMembers(Map<String, Object> conditions, OrderBy orderBy,
                                                   Integer start, Integer limit) {

        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions)
                .select("*")
                .orderBy(orderBy)
                .setFirstResult(start)
                .setMaxResults(limit);
        return getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
    }

    @Override
    public List<Map<String, Object>> searchMember(Map<String, Object> conditions, Integer start, Integer limit) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions)
                .select("*")
                .setFirstResult(start)
                .setMaxResults(limit)
                .orderBy(OrderBy.build(1).addAsc("createdTime"));
        return getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
    }

    @Override
    public Integer findMemberCountByUserIdAndRole(Integer userId, String role, boolean onlyPublished) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT( m.courseId ) FROM ").append(TABLE_NAME).append(" m ");
        sql.append(" JOIN  ").append(CourseDaoImpl.TABLE_NAME).append(" AS c ON m.userId = ? ");
        sql.append(" AND m.role =  ? AND m.courseId = c.id ");
        if (onlyPublished) {
            sql.append(" AND c.status = 'published' ");
        }
        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class, userId, role);
    }

    @Override
    public Integer findMemberCountByUserIdAndCourseTypeAndIsLearned(Integer userId, String role, String type, Integer isLearned) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT( m.courseId ) FROM ").append(TABLE_NAME).append(" m ");
        sql.append(" JOIN  ").append(CourseDaoImpl.TABLE_NAME).append(" AS c ON m.userId = ? ");
        sql.append(" AND c.type =  ? AND m.courseId = c.id  AND m.isLearned = ? AND m.role = ?");

        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class, userId, type, isLearned, role);
    }

    @Override
    public Integer findMemberCountByUserIdAndRoleAndIsLearned(Integer userId, String role, Integer isLearned) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE  userId = ? AND role = ? AND isLearned = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, userId, role, isLearned);
    }

    @Override
    public List<Map<String, Object>> findMembersByUserIdAndRole(Integer userId, String role, Integer start, Integer limit, boolean onlyPublished) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m.* FROM ").append(TABLE_NAME).append(" m ");
        sql.append(" JOIN  ").append(CourseDaoImpl.TABLE_NAME).append(" AS c ON m.userId = ? ");
        sql.append(" AND m.role =  ? AND m.courseId = c.id ");
        if (onlyPublished) {
            sql.append(" AND c.status = 'published' ");
        }
        sql.append(" ORDER BY createdTime DESC LIMIT ").append(start).append(", ").append(limit);

        return getJdbcTemplate().queryForList(sql.toString(), userId, role);
    }

    @Override
    public List<Map<String, Object>> findMembersByUserIdAndCourseTypeAndIsLearned(Integer userId, String role,
                                                                                  String type, Integer isLearned,
                                                                                  Integer start, Integer limit) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m.* FROM ").append(TABLE_NAME).append(" m ");
        sql.append(" JOIN  ").append(CourseDaoImpl.TABLE_NAME).append(" AS c ON m.userId = ? ");
        sql.append(" AND c.type =  ? AND m.courseId = c.id AND m.isLearned = ? AND m.role = ?");
        sql.append(" ORDER BY createdTime DESC LIMIT ").append(start).append(", ").append(limit);

        return getJdbcTemplate().queryForList(sql.toString(), userId, type, isLearned, role);
    }

    @Override
    public List<Map<String, Object>> findMembersByUserIdAndRoleAndIsLearned(Integer userId, String role,
                                                                            Integer isLearned,
                                                                            Integer start, Integer limit) {

        String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE userId = ? AND role = ? AND isLearned = ? ORDER BY createdTime DESC LIMIT " + start + ", " + limit;
        return getJdbcTemplate().queryForList(sql, userId, role, isLearned);
    }

    @Override
    public void updateMember(Object id, Map<String, Object> member) {
        updateMap(TABLE_NAME, member, "id", id);
    }

    @Override
    public Integer findMemberCountByCourseIdAndRole(Integer courseId, String role) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE  courseId = ? AND role = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, courseId, role);
    }

    @Override
    public void deleteMembersByCourseId(Integer courseId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE courseId = ?";
        getJdbcTemplate().update(sql, courseId);
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
