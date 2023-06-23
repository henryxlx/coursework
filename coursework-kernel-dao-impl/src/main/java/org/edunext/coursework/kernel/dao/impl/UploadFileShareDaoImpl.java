package org.edunext.coursework.kernel.dao.impl;

import org.edunext.coursework.kernel.dao.UploadFileShareDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class UploadFileShareDaoImpl implements UploadFileShareDao {

    static final String TABLE_NAME = "app_upload_file_share";

    @Override
    public List<Map<String, Object>> findMySharingContacts(Object currentUserId) {
        return null;
    }
}
