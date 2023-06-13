package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.FavoriteDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class FavoriteDaoImpl extends FastJdbcDaoSupport implements FavoriteDao {

    @Override
    public Integer getFavoriteCourseCountByUserId(Integer userId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findCourseFavoritesByUserId(Integer userId, Integer start, Integer limit) {
        return new ArrayList<>(0);
    }

    @Override
    public Map<String, Object> getFavoriteByUserIdAndCourseId(Integer userId, Integer courseId) {
        return null;
    }

    @Override
    public void addFavorite(Map<String, Object> fields) {

    }

    @Override
    public void deleteFavorite(Object id) {

    }
}
