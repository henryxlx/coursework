package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface TestPaperItemDao {

    void deleteItemsByTestPaperId(Object testPaperId);

    Map<String, Object> addItem(Map<String, Object> item);

    List<Map<String, Object>> findItemsByTestPaperId(Object testPaperId);

    Map<String, Object> updateItem(Object id, Map<String, Object> item);

    void deleteItem(Object id);
}
