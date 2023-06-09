package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface ReviewDao {

    Integer getReviewCountByCourseId(Integer courseId);

    List<Map<String, Object>> findReviewsByCourseId(Integer courseId, Integer start, Integer limit);

    Map<String, Object> getReviewByUserIdAndCourseId(Integer userId, Integer courseId);

    int addReview(Map<String, Object> fields);

    int updateReview(Object id, Map<String, Object> fields);

    int getReviewRatingSumByCourseId(Integer courseId);
}
