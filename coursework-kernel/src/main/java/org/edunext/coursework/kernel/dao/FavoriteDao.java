package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface FavoriteDao {

    Integer getFavoriteCourseCountByUserId(Integer userId);

    List<Map<String, Object>> findCourseFavoritesByUserId(Integer userId, Integer start, Integer limit);

    Map<String, Object> getFavoriteByUserIdAndCourseId(Integer userId, Integer courseId);

    void addFavorite(Map<String, Object> fields);

    void deleteFavorite(Object id);
}
