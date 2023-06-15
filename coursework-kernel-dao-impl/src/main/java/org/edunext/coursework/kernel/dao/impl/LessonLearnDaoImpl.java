package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.LessonLearnDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class LessonLearnDaoImpl extends FastJdbcDaoSupport implements LessonLearnDao {


    @Override
    public Integer findLearnsCountByLessonId(Integer lessonId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findLearnsByLessonId(Integer lessonId, Integer start, Integer limit) {
        return new ArrayList<>(0);
    }

    @Override
    public Integer getLearnCountByUserIdAndCourseIdAndStatus(Object userId, Object courseId, String status) {
        return 0;
    }

    @Override
    public void deleteLearnsByLessonId(Integer lessonId) {

    }
}
