package org.edunext.coursework.kernel.dao.impl;

import org.edunext.coursework.kernel.dao.TestPaperItemResultDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class TestPaperItemResultDaoImpl implements TestPaperItemResultDao {

    @Override
    public List<Map<String, Object>> findTestResultsByTestpaperResultId(Integer testpaperResultId) {
        return new ArrayList<>(0);
    }
}
