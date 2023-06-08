package org.edunext.coursework.kernel.service;

import com.jetwinner.util.MapUtil;
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

    @Override
    public Map<String, Object> getUserCourseReview(Integer userId, Integer courseId) {
        return MapUtil.newHashMap(0);
    }

    @Override
    public int saveReview(Map<String, Object> fields) {
        return 0;
    }
}
