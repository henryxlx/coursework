package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface UploadFileDao {

    Integer searchFileCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchFiles(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);

    Map<String, Object> addFile(Map<String, Object> file);
}
