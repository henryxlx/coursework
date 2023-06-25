package org.edunext.coursework.kernel.service.question.finder;

import com.jetwinner.util.FastHashMap;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Component("courseTargetFinder")
@Lazy
public class CourseTargetFinder extends AbstractTargetFinder {

    private final CourseService courseService;

    public CourseTargetFinder(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public Map<String, Map<String, Object>> find(Set<Object> ids) {
        List<Map<String, Object>> courses = this.courseService.findCoursesByIds(ids);
        Map<String, Map<String, Object>> targets = null;
        if (courses != null && courses.size() > 0) {
            targets = new HashMap<>(courses.size());
            for (Map<String, Object> course : courses) {
                String id = String.valueOf(course.get("id"));
                targets.put(id, FastHashMap.build(6)
                        .add("type", "course")
                        .add("id", id)
                        .add("simple_name", course.get("title"))
                        .add("name", course.get("title"))
                        .add("full_name", course.get("title"))
                        .add("url", "/course/" + id).toMap());
            }
        }
        return targets == null ? new HashMap<>(0) : targets;
    }
}
