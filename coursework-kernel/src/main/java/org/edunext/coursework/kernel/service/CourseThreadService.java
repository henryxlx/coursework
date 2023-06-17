package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.kernel.AppUser;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseThreadService {

    int searchThreadCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchThreads(Map<String, Object> conditions, String sort, Integer start, Integer limit);

    Map<String, Object> createThread(Map<String, Object> thread, AppUser currentUser);

    int searchThreadCountInCourseIds(Map<String, Object> conditions);

    List<Map<String, Object>> searchThreadInCourseIds(Map<String, Object> conditions, String sort,
                                                      Integer start, Integer limit);

    Map<String, Object> getThread(Integer courseId, Integer threadId);

    Integer getThreadPostCount(Integer courseId, Integer threadId);

    List<Map<String, Object>> findThreadPosts(Integer courseId, Integer threadId, String sort, Integer start, Integer limit);

    List<Map<String, Object>> findThreadElitePosts(Integer courseId, Integer threadId, int start, int limit);

    void hitThread(Integer courseId, Integer threadId);

    Integer getThreadPostCountByThreadId(Integer threadId);

    Integer getPostCountByUserIdAndThreadId(Integer userId, Integer threadId);

    Map<String, Object> createPost(Map<String, Object> post, AppUser currentUser);

    void deleteThread(Integer threadId, AppUser currentUser);

    Map<String, Object> getPost(Integer courseId, Integer id);

    void deletePost(Integer courseId, Integer id, AppUser currentUser);
}
