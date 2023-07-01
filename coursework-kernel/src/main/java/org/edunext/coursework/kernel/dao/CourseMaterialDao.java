package org.edunext.coursework.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseMaterialDao {

    Map<String, Object> getMaterial(Object id);

    Integer getMaterialCountByCourseId(Integer courseId);

    List<Map<String, Object>> findMaterialsByCourseId(Integer courseId, Integer start, Integer limit);

    Integer getMaterialCountByFileId(Object fileId);

    List<Map<String, Object>> findMaterialsByLessonId(Integer lessonId, Integer start, Integer limit);

    Map<String, Object> addMaterial(Map<String, Object> fields);

    void deleteMaterial(Object id);

    Integer getLessonMaterialCount(Integer courseId, Integer lessonId);

    int deleteMaterialsByCourseId(Integer courseId);

    int deleteMaterialsByLessonId(Integer lessonId);
}
