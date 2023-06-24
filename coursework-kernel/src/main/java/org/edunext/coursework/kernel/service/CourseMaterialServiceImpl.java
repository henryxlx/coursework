package org.edunext.coursework.kernel.service;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import org.edunext.coursework.kernel.dao.CourseMaterialDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class CourseMaterialServiceImpl implements CourseMaterialService {

    private final CourseMaterialDao materialDao;
    private final CourseService courseService;
    private final UploadFileService uploadFileService;

    public CourseMaterialServiceImpl(CourseMaterialDao materialDao,
                                     CourseService courseService,
                                     UploadFileService uploadFileService) {

        this.materialDao = materialDao;
        this.courseService = courseService;
        this.uploadFileService = uploadFileService;
    }

    @Override
    public Integer getMaterialCount(Integer courseId) {
        return this.materialDao.getMaterialCountByCourseId(courseId);
    }

    @Override
    public List<Map<String, Object>> findCourseMaterials(Integer courseId, Integer start, Integer limit) {
        return this.materialDao.findMaterialsByCourseId(courseId, start, limit);
    }

    @Override
    public Integer getMaterialCountByFileId(Object fileId) {
        return this.materialDao.getMaterialCountByFileId(fileId);
    }

    @Override
    public List<Map<String, Object>> findLessonMaterials(Integer lessonId, Integer start, Integer limit) {
        return this.materialDao.findMaterialsByLessonId(lessonId, start, limit);
    }

    @Override
    public Map<String, Object> uploadMaterial(Map<String, Object> material) {
        if (!ArrayToolkit.required(material, "courseId", "fileId")) {
            throw new RuntimeGoingException("参数缺失，上传失败！");
        }

        Map<String, Object> course = this.courseService.getCourse(ValueParser.toInteger(material.get("courseId")));
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在，上传资料失败！");
        }

        Map<String, Object> fields = new HashMap<>(10);
        fields.put("courseId", material.get("courseId"));
        fields.put("lessonId", EasyStringUtil.isBlank(material.get("lessonId")) ? 0 : material.get("lessonId"));
        fields.put("description", EasyStringUtil.isBlank(material.get("description")) ? "" : material.get("description"));
        fields.put("userId", AppUser.getCurrentUser(material).getId());
        fields.put("createdTime", System.currentTimeMillis());

        int materialFileId = ValueParser.parseInt(material.get("fileId"));
        if (materialFileId < 1) {
            if (EasyStringUtil.isBlank(material.get("link"))) {
                throw new RuntimeGoingException("资料链接地址不能为空，添加资料失败！");
            }
            fields.put("fileId", 0);
            fields.put("link", material.get("link"));
            fields.put("title", EasyStringUtil.isBlank(material.get("description")) ?
                    material.get("link") : material.get("description"));
        } else {
            fields.put("fileId", materialFileId);
            Map<String, Object> file = this.uploadFileService.getFile(materialFileId);
            if (MapUtil.isEmpty(file)) {
                throw new RuntimeGoingException("文件不存在，上传资料失败！");
            }
            fields.put("link", "");
            fields.put("title", file.get("filename"));
            fields.put("fileSize", file.get("size"));
        }

        material = this.materialDao.addMaterial(fields);

        // Increase the linked file usage count, if there"s a linked file used by this material.
        if (ValueParser.parseInt(material.get("fileId")) > 0) {
            this.uploadFileService.increaseFileUsedCount(materialFileId);
        }

        this.courseService.increaseLessonMaterialCount(fields.get("lessonId"));

        return material;
    }

    @Override
    public void deleteMaterial(Integer courseId, Integer materialId) {
        Map<String, Object> material = this.materialDao.getMaterial(materialId);
        if (MapUtil.isEmpty(material) || ValueParser.parseInt(material.get("courseId")) != courseId) {
            throw new RuntimeGoingException("课程资料不存在，删除失败。");
        }
        this.materialDao.deleteMaterial(materialId);

        // Decrease the linked file usage count, if there's a linked file used by this material.
        Integer materialFileId = ValueParser.toInteger(material.get("fileId"));
        if (materialFileId > 0) {
            this.uploadFileService.decreaseFileUsedCount(materialFileId);
        }

        Integer materialLessonId = ValueParser.toInteger(material.get("lessonId"));
        if (materialLessonId > 0) {
            int count = this.materialDao.getLessonMaterialCount(courseId, materialLessonId);
            this.courseService.resetLessonMaterialCount(materialLessonId, count);
        }
    }

    @Override
    public Map<String, Object> getMaterial(Integer courseId, Integer materialId) {
        Map<String, Object> material = this.materialDao.getMaterial(materialId);
        if (MapUtil.isEmpty(material) || ValueParser.parseInt(material.get("courseId")) != courseId) {
            return null;
        }
        return material;
    }
}
