package org.edunext.coursework.kernel.service;

import org.edunext.coursework.kernel.service.upload.UploadFileHandlerAware;
import org.edunext.coursework.kernel.service.upload.UploadFileHandlerTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface UploadFileService {

    default UploadFileHandlerAware getImpl() {
        return getImpl(UploadFileHandlerTypeEnum.LOCAL);
    }

    UploadFileHandlerAware getImpl(UploadFileHandlerTypeEnum uploadFileHandlerType);

    List<Map<String, Object>> searchFiles(Map<String, Object> conditions, String sort, Integer start, Integer limit);
}
