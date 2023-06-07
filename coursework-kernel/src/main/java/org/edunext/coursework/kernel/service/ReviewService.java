package org.edunext.coursework.kernel.service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface ReviewService {

    Integer getCourseReviewCount(Integer courseId);

    List<Map<String, Object>> findCourseReviews(Integer courseId, Integer start, Integer limit);
}
