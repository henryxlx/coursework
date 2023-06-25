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
@Component("lessonTargetFinder")
@Lazy
public class LessonTargetFinder extends AbstractTargetFinder {

    private final CourseService courseService;

    public LessonTargetFinder(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public Map<String, Map<String, Object>> find(Set<Object> ids) {
        List<Map<String, Object>> lessons = this.courseService.findLessonsByIds(ids);
        Map<String, Map<String, Object>> targets = null;
        if (lessons != null && lessons.size() > 0) {
            targets = new HashMap<>(lessons.size());
            for (Map<String, Object> lesson : lessons) {
                String id = String.valueOf(lesson.get("id"));
                targets.put(id, FastHashMap.build(6)
                        .add("type", "lesson")
                        .add("id", id)
                        .add("simple_name", "课时" + lesson.get("number"))
                        .add("name", lesson.get("title"))
                        .add("full_name", "课时" + lesson.get("number") + "：" + lesson.get("title"))
                        .add("url", "/course/" + lesson.get("courseId") + "/learn#lesson/" + id).toMap());
            }
        }
        return targets == null ? new HashMap<>(0) : targets;
    }
}
