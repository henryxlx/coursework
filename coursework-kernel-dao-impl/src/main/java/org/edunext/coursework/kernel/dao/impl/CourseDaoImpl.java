package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.CourseDao;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class CourseDaoImpl extends FastJdbcDaoSupport implements CourseDao {

    private static final String TABLE_NAME = "cw_course";

    @Override
    public int searchCourseCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = createSearchQueryBuilder(conditions)
            .select("COUNT(id)");
        return getJdbcTemplate().queryForObject(builder.getSQL(), Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchCourses(Map<String, Object> conditions,
                                                   OrderBy orderBy, Integer start, Integer limit) {

        DynamicQueryBuilder builder = createSearchQueryBuilder(conditions)
            .select("*")
            .orderBy(orderBy)
            .setFirstResult(start)
            .setMaxResults(limit);
        /**if ($orderBy[0] == 'recommendedSeq') {
            builder.addOrderBy('recommendedTime', 'DESC');
        }*/
        return getJdbcTemplate().queryForList(builder.getSQL());
    }

    @Override
    public Map<String, Object> addCourse(Map<String, Object> course) {
        Integer id = insertMapReturnKey("cw_course", course).intValue();
        return getCourse(id);
    }

    @Override
    public Map<String, Object> getCourse(Object id) {
        return getJdbcTemplate().queryForList("SELECT * FROM cw_course WHERE id = ? LIMIT 1", id)
                .stream().findFirst().orElse(Collections.emptyMap());
    }

    private DynamicQueryBuilder createSearchQueryBuilder(Map<String, Object> conditions) {
        if (EasyStringUtil.isNotBlank(conditions.get("title"))) { 
            conditions.put("titleLike", "%" + conditions.get("title") + "%");
            conditions.remove("title");
        }

        if (EasyStringUtil.isNotBlank(conditions.get("tagId"))) {  
            int tagId = ValueParser.parseInt(conditions.get("tagId"));
            if (tagId > 0) {
                conditions.put("tagsLike", "%" + conditions.get("tagId") + "%");
            }
            conditions.remove("tagId");
        }

        if (EasyStringUtil.isNotBlank(conditions.get("tagIds"))) {
            Object[] tagIds = (Object[]) conditions.get("tagIds");
            StringBuilder buf = new StringBuilder();
            buf.append("%|");
            if (tagIds != null) {
                for (Object tagId : tagIds) {
                    buf.append(tagId).append("|");
                }
            }
            buf.append("%");  
            conditions.put("tagsLike", buf.toString());
            conditions.remove("tagIds");
        }

        DynamicQueryBuilder builder = new DynamicQueryBuilder(conditions)
            .from(TABLE_NAME, "course")
            .andWhere("status = :status")
            .andWhere("type = :type")
            .andWhere("price = :price")
            .andWhere("price > :price_GT")
            .andWhere("originPrice > :originPrice_GT")
            .andWhere("coinPrice > :coinPrice_GT")
            .andWhere("originCoinPrice > :originCoinPrice_GT")
            .andWhere("title LIKE :titleLike")
            .andWhere("userId = :userId")
            .andWhere("recommended = :recommended")
            .andWhere("tags LIKE :tagsLike")
            .andWhere("startTime >= :startTimeGreaterThan")
            .andWhere("startTime < :startTimeLessThan")
            .andWhere("rating > :ratingGreaterThan")
            .andWhere("vipLevelId >= :vipLevelIdGreaterThan")
            .andWhere("vipLevelId = :vipLevelId")
            .andWhere("createdTime >= :startTime")
            .andWhere("createdTime <= :endTime")
            .andWhere("categoryId = :categoryId")
            .andWhere("smallPicture = :smallPicture")
            .andWhere("categoryId IN ( :categoryIds )")
            .andWhere("vipLevelId IN ( :vipLevelIds )")
            .andWhere("id NOT IN ( :courseIds )");

        return builder;
    }
}
