package org.edunext.coursework.kernel.event;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.webfast.event.EventSubscriber;
import com.jetwinner.webfast.event.FastEventHandler;
import com.jetwinner.webfast.event.ServiceEvent;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.service.AppUserStatusService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xulixin
 */
@Component
public class CourseEventSubscriber implements EventSubscriber, InitializingBean, DisposableBean {

    @Override
    public Map<String, String> getSubscribedEvents() {
        Map<String, String> map = new HashMap<>(4);
        map.put("course.lesson_start", "onLessonStart");
        map.put("course.lesson_finish", "onLessonFinish");
        map.put("course.join", "onCourseJoin");
        map.put("course.favorite", "onCourseFavorite");
        return map;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FastEventHandler.getDefault().register(this);
    }

    @Override
    public void destroy() throws Exception {
        FastEventHandler.getDefault().unregister(this);
    }

    private final AppUserStatusService statusService;

    public CourseEventSubscriber(AppUserStatusService statusService) {
        this.statusService = statusService;
    }

    public void onLessonStart(ServiceEvent event) {
        Map<String, Object> lesson = event.getSubject();
        Map<String, Object> course = event.getArgumentAsMap("course");
        AppUser currentUser = event.getCurrentUser();
        this.statusService.publishStatus(FastHashMap.build(5)
                .add("type", "start_learn_lesson")
                .add("objectType", "lesson")
                .add("objectId", lesson.get("id"))
                .add("private", "published".equals(course.get("status")) ? 0 : 1)
                .add("properties", FastHashMap.build(2)
                        .add("course", this.simplifyCourse(course))
                        .add("lesson", this.simplifyLesson(lesson)).toMap()).toMap(), currentUser);
    }

    public void onLessonFinish(ServiceEvent event) {
        Map<String, Object> lesson = event.getSubject();
        Map<String, Object> course = event.getArgumentAsMap("course");
        AppUser currentUser = event.getCurrentUser();
        this.statusService.publishStatus(FastHashMap.build(5)
                .add("type", "learned_lesson")
                .add("objectType", "lesson")
                .add("objectId", lesson.get("id"))
                .add("private", "published".equals(course.get("status")) ? 0 : 1)
                .add("properties", FastHashMap.build(2)
                        .add("course", this.simplifyCourse(course))
                        .add("lesson", this.simplifyLesson(lesson)).toMap()).toMap(), currentUser);
    }

    public void onCourseFavorite(ServiceEvent event) {
        Map<String, Object> course = event.getSubject();
        AppUser currentUser = event.getCurrentUser();
        this.statusService.publishStatus(FastHashMap.build(5)
                .add("type", "favorite_course")
                .add("objectType", "course")
                .add("objectId", course.get("id"))
                .add("private", "published".equals(course.get("status")) ? 0 : 1)
                .add("properties", FastHashMap.build(1)
                        .add("course", this.simplifyCourse(course)).toMap()).toMap(), currentUser);
    }

    public void onCourseJoin(ServiceEvent event) {
        Map<String, Object> course = event.getSubject();
        Object userId = event.getArgument("userId");
        AppUser currentUser = event.getCurrentUser();
        this.statusService.publishStatus(FastHashMap.build(6)
                .add("type", "become_student")
                .add("objectType", "course")
                .add("objectId", course.get("id"))
                .add("private", "published".equals(course.get("status")) ? 0 : 1)
                .add("userId", userId)
                .add("properties", FastHashMap.build(1)
                        .add("course", this.simplifyCourse(course)).toMap()).toMap(), currentUser);
    }

    private Map<String, Object> simplifyCourse(Map<String, Object> course) {
        return FastHashMap.build(7)
                .add("id", course.get("id"))
                .add("title", course.get("title"))
                .add("picture", course.get("middlePicture"))
                .add("type", course.get("type"))
                .add("rating", course.get("rating"))
                .add("about", EasyStringUtil.plainTextFilter(course.get("about"), 100))
                .add("price", course.get("price")).toMap();
    }

    private Map<String, Object> simplifyLesson(Map<String, Object> lesson) {
        return FastHashMap.build(5).add("id", lesson.get("id"))
                .add("number", lesson.get("number"))
                .add("type", lesson.get("type"))
                .add("title", lesson.get("title"))
                .add("summary", EasyStringUtil.plainTextFilter(lesson.get("summary"), 100)).toMap();
    }
}
