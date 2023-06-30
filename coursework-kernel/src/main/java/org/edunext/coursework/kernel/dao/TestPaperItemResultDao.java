package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface TestPaperItemResultDao {

    List<Map<String, Object>> findTestResultsByTestpaperResultId(Integer testpaperResultId);
}
