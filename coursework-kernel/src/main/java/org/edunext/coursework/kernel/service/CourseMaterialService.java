package org.edunext.coursework.kernel.service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface CourseMaterialService {

    Integer getMaterialCount(Integer courseId);

    List<Map<String, Object>> findCourseMaterials(Integer courseId, Integer start, Integer limit);
}
