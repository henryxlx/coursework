package org.edunext.coursework.kernel.service;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import org.edunext.coursework.kernel.dao.ReviewDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;
    private final CourseService courseService;
    private final AppUserService userService;

    public ReviewServiceImpl(ReviewDao reviewDao,
                             CourseService courseService,
                             AppUserService userService) {

        this.reviewDao = reviewDao;
        this.courseService = courseService;
        this.userService = userService;
    }

    @Override
    public Integer getCourseReviewCount(Integer courseId) {
        return this.reviewDao.getReviewCountByCourseId(courseId);
    }

    @Override
    public List<Map<String, Object>> findCourseReviews(Integer courseId, Integer start, Integer limit) {
        return this.reviewDao.findReviewsByCourseId(courseId, start, limit);
    }

    @Override
    public Map<String, Object> getUserCourseReview(Integer userId, Integer courseId) {
        AppUser user = this.userService.getUser(userId);
        if (user == null) {
            throw new RuntimeGoingException("User is not Exist!");
        }
        Map<String, Object> course = this.courseService.getCourse(courseId);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("Course is not Exist!");
        }
        return this.reviewDao.getReviewByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public int saveReview(Map<String, Object> fields) {
        if (!ArrayToolkit.required(fields, "courseId", "userId", "rating")) {
            throw new RuntimeGoingException("参数不正确，评价失败！");
        }

        Map<String, Object> course = this.courseService.getCourse(ValueParser.parseInt(fields.get("courseId")));

        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程(#" + fields.get("courseId") + ")不存在，评价失败！");
        }
        AppUser user = this.userService.getUser(fields.get("userId"));
        if (user == null) {
            throw new RuntimeGoingException("用户(#" + fields.get("courseId") + ")不存在,评价失败!");
        }

        int nums = 0;
        Map<String, Object> review = this.reviewDao.getReviewByUserIdAndCourseId(user.getId(), ValueParser.toInteger(course.get("id")));
        if (MapUtil.isEmpty(review)) {
            nums = this.reviewDao.addReview(new ParamMap()
                    .add("userId", fields.get("userId"))
                    .add("courseId", fields.get("courseId"))
                    .add("rating", fields.get("rating"))
                    .add("private", "published".equals(course.get("status")) ? 0 : 1)
                    .add("content", EasyStringUtil.isBlank(fields.get("content")) ? "" : fields.get("content"))
                    .add("createdTime", System.currentTimeMillis()).toMap());
        } else {
            nums = this.reviewDao.updateReview(review.get("id"), new ParamMap()
                    .add("content", EasyStringUtil.isBlank(fields.get("content")) ? "" : fields.get("content"))
                    .add("rating", fields.get("rating")).toMap());
        }

        this.calculateCourseRating(ValueParser.toInteger(course.get("id")));
        return nums;
    }

    @Override
    public int searchReviewsCount(Map<String, Object> conditions) {
        conditions = this.prepareReviewSearchConditions(conditions);
        return this.reviewDao.searchReviewsCount(conditions);
    }

    @Override
    public List<Map<String, Object>> searchReviews(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        OrderBy orderBy = OrderBy.build(1);
        if ("latest".equals(sort)) {
            orderBy.addDesc("createdTime");
        } else {
            orderBy.addDesc("rating");
        }
        conditions = this.prepareReviewSearchConditions(conditions);
        return this.reviewDao.searchReviews(conditions, orderBy, start, limit);
    }

    private void calculateCourseRating(Integer courseId) {
        int ratingSum = this.reviewDao.getReviewRatingSumByCourseId(courseId);
        int ratingNum = this.reviewDao.getReviewCountByCourseId(courseId);

        this.courseService.updateCourseCounter(courseId, new ParamMap()
                .add("rating", ratingNum > 0 ? ratingSum / ratingNum : 0)
                .add("ratingNum", ratingNum).toMap());
    }

    private Map<String, Object> prepareReviewSearchConditions(Map<String, Object> conditions) {
        Set<String> keys = conditions.keySet();
        for (String key : keys) {
            if (!EasyStringUtil.isNumeric(String.valueOf(conditions.get(key)))) {
                conditions.remove(key);
            }
        }

        if (EasyStringUtil.isNotBlank(conditions.get("author"))) {
            AppUser author = this.userService.getUserByUsername(String.valueOf(conditions.get("author")));
            conditions.put("userId", author != null ? author.getId() : -1);
        }

        return conditions;
    }
}
