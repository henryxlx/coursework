package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.dao.support.DynamicQueryBuilder;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.UploadFileDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class UploadFileDaoImpl extends FastJdbcDaoSupport implements UploadFileDao {

    static final String TABLE_NAME = "app_upload_file";

    @Override
    public Integer searchFileCount(Map<String, Object> conditions) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions)
                .select("COUNT(id)");
        return getNamedParameterJdbcTemplate().queryForObject(builder.getSQL(), conditions, Integer.class);
    }

    @Override
    public List<Map<String, Object>> searchFiles(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit) {
        DynamicQueryBuilder builder = this.createSearchQueryBuilder(conditions)
                .select("*")
                .orderBy(orderBy)
                .setFirstResult(start)
                .setMaxResults(limit);
        return getNamedParameterJdbcTemplate().queryForList(builder.getSQL(), conditions);
    }

    @Override
    public Map<String, Object> getFile(Object id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ? LIMIT 1";
        return getJdbcTemplate().queryForList(sql, id).stream().findFirst().orElse(null);
    }

    @Override
    public int deleteFile(Object id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        return getJdbcTemplate().update(sql, id);
    }

    @Override
    public int findFilesCountByEtag(Object etag) {
        if (EasyStringUtil.isBlank(etag)) {
            return 0;
        }

        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE etag = ? ";
        return getJdbcTemplate().queryForObject(sql, Integer.class, etag);
    }

    @Override
    public Map<String, Object> addFile(Map<String, Object> file) {
        file.put("createdTime", System.currentTimeMillis());
        Integer id = insertMapReturnKey(TABLE_NAME, file).intValue();
        return this.getFile(id);
    }

    private DynamicQueryBuilder createSearchQueryBuilder(Map<String, Object> conditions) {
        if (EasyStringUtil.isNotBlank(conditions.get("filename"))) {
            conditions.put("filenameLike", "%" + conditions.get("filename") + "%");
            conditions.remove("filename");
        }

        return new DynamicQueryBuilder(conditions)
                .from(TABLE_NAME)
                .andWhere("targetType = :targetType")
                .andWhere("targetId = :targetId")
                .andWhere("type = :type")
                .andWhere("storage = :storage")
                .andWhere("filename LIKE :filenameLike")
                .andWhere("createdUserId in ( :createdUserIds )");
    }
}
