package org.edunext.coursework.kernel.service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface ReviewService {

    Integer getCourseReviewCount(Integer courseId);

    List<Map<String, Object>> findCourseReviews(Integer courseId, Integer start, Integer limit);

    Map<String, Object> getUserCourseReview(Integer userId, Integer courseId);

    int saveReview(Map<String, Object> fields);

    int searchReviewsCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchReviews(Map<String, Object> conditions, String sort, Integer start, Integer limit);
}
