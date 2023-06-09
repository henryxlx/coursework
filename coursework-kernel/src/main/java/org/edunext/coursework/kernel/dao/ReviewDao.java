package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

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

    int searchReviewsCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchReviews(Map<String, Object> conditions, OrderBy orderBy, Integer start, Integer limit);
}
