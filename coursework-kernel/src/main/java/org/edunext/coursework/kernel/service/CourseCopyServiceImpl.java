package org.edunext.coursework.kernel.service;

import com.jetwinner.toolbag.ArrayToolkit;
import org.edunext.coursework.kernel.dao.CourseDao;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class CourseCopyServiceImpl implements CourseCopyService {

    private final CourseDao courseDao;

    public CourseCopyServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Map<String, Object> copyCourse(Map<String, Object> course) {
        Map<String, Object> fields = ArrayToolkit.part(course, "title", "subtitle", "type", "maxStudentNum",
                "price", "coinPrice", "expiryDay", "serializeMode", "lessonNum", "giveCredit", "vipLevelId",
                "categoryId", "tags", "smallPicture", "middlePicture", "largePicture", "about", "teacherIds",
                "goals", "audiences", "userId");

        fields.put("status", "draft");
        fields.put("createdTime", System.currentTimeMillis());

        return courseDao.addCourse(fields);
    }
}
