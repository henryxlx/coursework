package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseChapterDao {

    List<Map<String, Object>> findChaptersByCourseId(Object courseId);

    Map<String, Object> addChapter(Map<String, Object> chapter);

    Map<String, Object> getLastChapterByCourseIdAndType(Object courseId, String type);

    int getChapterCountByCourseIdAndTypeAndParentId(Object courseId, String type, int parentId);

    int getChapterCountByCourseIdAndType(Object courseId, String type);

    int getChapterMaxSeqByCourseId(Object courseId);

    Map<String, Object> getChapter(Object chapterId);

    int deleteChapter(Object chapterId);

    Map<String, Object> updateChapter(Integer chapterId, Map<String, Object> fields);
}
