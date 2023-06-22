package org.edunext.coursework.kernel.service;

import org.edunext.coursework.kernel.service.upload.DummyUploadFileService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
public interface UploadFileService {

    default UploadFileService getImpl() {
        return getImpl(UploadFileType.LOCAL);
    }

    default UploadFileService getImpl(UploadFileType uploadFileType) {
        ApplicationContext applicationContext = getApplicationContext();
        UploadFileService fileService = applicationContext != null ?
                applicationContext.getBean(uploadFileType.getName(), UploadFileService.class) : null;
        if (fileService == null) {
            fileService = new DummyUploadFileService();
        }
        return fileService;
    }

    ApplicationContext getApplicationContext();

    Map<String, Object> makeUploadParams(Map<String, Object> params);

    Map<String, Object> addFile(String targetType, Integer targetId, Map fileInfo, MultipartFile originalFile);

    List<Map<String, Object>> searchFiles(Map<String, Object> conditions, String sort, Integer start, Integer limit);

    enum UploadFileType {
        LOCAL("localUploadFileServiceImpl"), CLOUD("cloudUploadFileServiceImpl");

        private String name;

        UploadFileType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    ;
}
