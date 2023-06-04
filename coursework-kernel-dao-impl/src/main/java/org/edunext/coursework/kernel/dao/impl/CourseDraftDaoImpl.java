package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.CourseDraftDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class CourseDraftDaoImpl extends FastJdbcDaoSupport implements CourseDraftDao {

    @Override
    public int deleteCourseDrafts(Integer courseId, Integer lessonId, Integer userId) {
        return 0;
    }

    @Override
    public Map<String, Object> findCourseDraft(Object courseId, Integer lessonId, Integer userId) {
        return null;
    }
}
