package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface UploadFileShareDao {

    List<Map<String, Object>> findMySharingContacts(Object currentUserId);
}
