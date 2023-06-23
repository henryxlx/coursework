package org.edunext.coursework.kernel.service.upload;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author xulixin
 */
public interface UploadFileHandlerAware {

    Map<String, Object> makeUploadParams(Map<String, Object> params);

    Map<String, Object> addFile(String targetType, Integer targetId, Map fileInfo, MultipartFile originalFile);
}
