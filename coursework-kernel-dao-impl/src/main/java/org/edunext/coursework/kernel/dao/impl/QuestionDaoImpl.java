package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.*;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.dao.QuestionDao;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author xulixin
 */
@Repository
public class QuestionDaoImpl extends FastJdbcDaoSupport implements QuestionDao {

    static final String TABLE_NAME = "cw_question";

    private Map<String, Object> unserialize(Map<String, Object> question) {
        Object obj = question.get("answer");
        if (obj != null) {
            question.put("answer", JsonUtil.jsonDecode(obj, List.class));
        }
        obj = question.get("metas");
        if (obj != null) {
            question.put("metas", JsonUtil.jsonDecodeMap(obj));
        }
        return question;
    }

    private void toArray(String key, Map<String, Object> map) {
        Object obj = map.get(key);
        if (obj != null) {
            if (!(ObjectUtils.isArray(obj) || obj instanceof Collection)) {
                map.put(key, new String[]{String.valueOf(obj)});
            }
        }
    }

    private Map<String, Object> serialize(Map<String, Object> fields) {
        toArray("answer", fields);
        String[] fieldNames = {"answer", "metas"};
        for (String fieldName : fieldNames) {
            if (fields.get(fieldName) != null) {
                fields.put(fieldName, JsonUtil.objectToString(fields.get(fieldName)));
            }
        }
        return fields;
    }

    @Override
    public Map<String, Object> getQuestion(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        Map<String, Object> question = getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
        return MapUtil.isNotEmpty(question) ? this.unserialize(question) : null;
    }

    @Override
    public Integer searchQuestionsCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions).select("COUNT(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchQuestions(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions)
                .select("*")
                .setFirstResult(start)
                .setMaxResults(limit)
                .orderBy(orderBy);

        return getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
    }

    @Override
    public Map<String, Object> addQuestion(Map<String, Object> fields) {
        fields = this.serialize(fields);
        Integer id = insertMapReturnKey(TABLE_NAME, fields).intValue();
        return this.getQuestion(id);
    }

    @Override
    public Integer findQuestionsCountByParentId(Integer parentId) {
        String sql = "SELECT count(*) FROM " + TABLE_NAME + " WHERE parentId = ?";
        return getJdbcTemplate().queryForObject(sql, Integer.class, parentId);
    }

    @Override
    public Map<String, Object> updateQuestion(Integer id, Map<String, Object> fields) {
        fields = this.serialize(fields);
        updateMap(TABLE_NAME, fields, "id", id);
        return this.getQuestion(id);
    }

    @Override
    public int deleteQuestion(Integer id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        return getJdbcTemplate().update(sql, id);
    }

    @Override
    public int deleteQuestionsByParentId(Integer parentId) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE parentId = ?";
        return getJdbcTemplate().update(sql, parentId);
    }

    @Override
    public List<Map<String, Object>> findQuestionsByParentId(Integer id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE parentId = ? ORDER BY createdTime ASC";
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, id);
        list.forEach(this::unserialize);
        return list;
    }

    @Override
    public List<Map<String, Object>> getQuestionCountGroupByTypes(Map<String, Object> conditions) {
        String sqlWhere = "";
        if (conditions.get("types") != null) {
            String marks = joinInStringValues(conditions.get("types"));
            sqlWhere = sqlWhere + " AND type IN (" + marks + ") ";
        }
        if (conditions.get("targets") != null) {
            String targetMarks = joinInStringValues(conditions.get("targets"));
            sqlWhere = sqlWhere + " AND target IN (" + targetMarks + ") ";
        }
        if (EasyStringUtil.isNotBlank(conditions.get("courseId"))) {
            sqlWhere = sqlWhere + " AND (target='course-" + conditions.get("courseId") +
                    "' or target like 'course-" + conditions.get("courseId") + "/%') ";
        }
        String sql = "SELECT COUNT(*) AS questionNum, type FROM " + TABLE_NAME + " WHERE parentId = '0' " + sqlWhere +
                " GROUP BY type ";
        return getJdbcTemplate().queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findQuestionsByIds(Set<Object> ids) {
        if (ids == null || ids.size() < 1) {
            return new ArrayList<>(0);
        }
        String marks = repeatQuestionMark(ids.size());
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id IN (" + marks + ");";
        List<Map<String, Object>> questions = getJdbcTemplate().queryForList(sql, ids.toArray());
        questions.forEach(this::unserialize);
        return questions;
    }

    @Override
    public void updateQuestionCountByIds(Set<String> ids, String status) {
        if (ids == null || ids.size() < 1) {
            return;
        }

        String[] fields = {"finishedTimes", "passedTimes"};
        if (!ArrayUtil.inArray(status, fields)) {
            throw new RuntimeGoingException(String.format("%s字段不允许增减，只有 %s 才被允许增减",
                    status, Arrays.toString(fields)));
        }

        String marks = repeatQuestionMark(ids.size());
        String sql = String.format("UPDATE %s SET %s = %s+1 WHERE id IN (%s)",
                TABLE_NAME, status, status, marks);
        getJdbcTemplate().update(sql, ids.toArray());
    }

    private DynamicQueryBuilder createSearchQueryBuilder(Map<String, Object> conditions) {
        if (EasyStringUtil.isNotBlank(conditions.get("targetPrefix"))) {
            conditions.put("targetLike", conditions.get("targetPrefix") + "/%");
            conditions.remove("target");
        }

        if (EasyStringUtil.isNotBlank(conditions.get("stem"))) {
            conditions.put("stem", "%" + conditions.get("stem") + "%");
        }

        if (conditions.get("targets") != null && conditions.get("targets").getClass().isArray()) {
            conditions.remove("target");
            conditions.remove("targetPrefix");
        }

        DynamicQueryBuilder builder = new DynamicQueryBuilder(conditions)
                .from(TABLE_NAME, "questions")
                .andWhere("target IN ( :targets )")
                .andWhere("target = :target")
                .andWhere("target = :targetPrefix OR target LIKE :targetLike")
                .andWhere("parentId = :parentId")
                .andWhere("difficulty = :difficulty")
                .andWhere("type = :type")
                .andWhere("stem LIKE :stem")
                .andWhere("type IN ( :types )")
                .andWhere("id NOT IN ( :excludeIds ) ");

        if (ValueParser.parseInt(conditions.get("excludeUnvalidatedMaterial")) == 1) {
            builder.andStaticWhere(" not( type = 'material' and subCount = 0 )");
        }
        return builder;
    }

}
