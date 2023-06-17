package org.edunext.coursework.kernel.dao;

import com.jetwinner.webfast.kernel.dao.support.OrderBy;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseThreadPostDao {

    Integer getPostCountByThreadId(Integer threadId);

    List<Map<String, Object>> findPostsByThreadId(Integer threadId, OrderBy orderBy, Integer start, Integer limit);

    Map<String, Object> findPostsByThreadIdAndIsElite(Integer threadId, Integer isElite, Integer start, Integer limit);

    Integer getPostCountByUserIdAndThreadId(Integer userId, Integer threadId);
}
