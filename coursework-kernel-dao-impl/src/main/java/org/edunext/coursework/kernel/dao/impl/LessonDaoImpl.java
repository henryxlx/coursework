package org.edunext.coursework.kernel.dao.impl;

import org.edunext.coursework.kernel.dao.LessonDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class LessonDaoImpl implements LessonDao {

    @Override
    public List<Map<String, Object>> findLessonsByCourseId(Object courseId) {
        return new ArrayList<>(0);
    }

    @Override
    public int getLessonMaxSeqByCourseId(Object courseId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findLessonsByChapterId(Object chapterId) {
        return null;
    }

    @Override
    public void batchUpdateLesson(List<Map<String, Object>> lessons, String pKeyName, String... updateFieldNames) {

    }
}
