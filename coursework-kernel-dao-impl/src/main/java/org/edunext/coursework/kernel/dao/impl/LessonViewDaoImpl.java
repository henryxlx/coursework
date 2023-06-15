package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.LessonViewDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class LessonViewDaoImpl extends FastJdbcDaoSupport implements LessonViewDao {

    @Override
    public Map<String, Object> getLessonView(Object id) {
        return MapUtil.newHashMap(0);
    }

    @Override
    public Map<String, Object> addLessonView(Map<String, Object> createLessonView) {
        return MapUtil.newHashMap(0);
    }
}
