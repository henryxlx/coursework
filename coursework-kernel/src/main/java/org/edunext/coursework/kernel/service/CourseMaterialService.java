package org.edunext.coursework.kernel.service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseMaterialService {

    Integer getMaterialCount(Integer courseId);

    List<Map<String, Object>> findCourseMaterials(Integer courseId, Integer start, Integer limit);

    Integer getMaterialCountByFileId(Object fileId);

    List<Map<String, Object>> findLessonMaterials(Integer lessonId, Integer start, Integer limit);

    Map<String, Object> uploadMaterial(Map<String, Object> material);

    void deleteMaterial(Integer courseId, Integer materialId);

    Map<String, Object> getMaterial(Integer courseId, Integer materialId);

    void deleteMaterialsByCourseId(Integer courseId);

    void deleteMaterialsByLessonId(Integer lessonId);

    void increaseLessonMaterialCount(Object lessonId);

    void resetLessonMaterialCount(Integer lessonId, Integer count);
}
