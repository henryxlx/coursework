package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.CourseNoteDao;
import org.springframework.stereotype.Repository;

/**
 * @author xulixin
 */
@Repository
public class CourseNoteDaoImpl extends FastJdbcDaoSupport implements CourseNoteDao {

    @Override
    public Integer getNoteCountByUserIdAndCourseId(Integer userId, Integer courseId) {
        return 0;
    }
}
