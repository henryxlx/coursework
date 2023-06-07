package org.edunext.coursework.kernel.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Override
    public Integer getCourseReviewCount(Integer courseId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findCourseReviews(Integer courseId, Integer start, Integer limit) {
        return null;
    }
}
