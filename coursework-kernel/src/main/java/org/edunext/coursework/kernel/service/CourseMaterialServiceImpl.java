package org.edunext.coursework.kernel.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class CourseMaterialServiceImpl implements CourseMaterialService {

    @Override
    public Integer getMaterialCount(Integer courseId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findCourseMaterials(Integer courseId, Integer start, Integer limit) {
        return new ArrayList<>(0);
    }

    @Override
    public Integer getMaterialCountByFileId(Object fileId) {
//        return this.materialDao.getMaterialCountByFileId(fileId);
        return 0;
    }
}
