package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.JsonUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.QuestionDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class QuestionDaoImpl extends FastJdbcDaoSupport implements QuestionDao {

    static final String TABLE_NAME = "cw_question";

    private Map<String, Object> unserialize(Map<String, Object> question) {
        Object obj = question.get("answer");
        if (obj != null) {
            question.put("answer", JsonUtil.jsonDecode(obj, Object[].class));
        }
        obj = question.get("metas");
        if (obj != null) {
            question.put("metas", JsonUtil.jsonDecodeMap(obj));
        }
        return question;
    }

    private Map<String, Object> serialize(Map<String, Object> fields) {
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
