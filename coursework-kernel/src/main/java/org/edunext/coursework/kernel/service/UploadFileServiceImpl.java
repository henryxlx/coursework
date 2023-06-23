package org.edunext.coursework.kernel.service;

import org.edunext.coursework.kernel.service.upload.DummyUploadHandler;
import org.edunext.coursework.kernel.service.upload.UploadFileHandlerAware;
import org.edunext.coursework.kernel.service.upload.UploadFileHandlerTypeEnum;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class UploadFileServiceImpl implements UploadFileService {

    private final ApplicationContext applicationContext;

    public UploadFileServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public UploadFileHandlerAware getImpl(UploadFileHandlerTypeEnum uploadFileHandlerType) {
        UploadFileHandlerAware uploadFileHandler = this.applicationContext != null ?
                this.applicationContext.getBean(uploadFileHandlerType.getName(), UploadFileHandlerAware.class) : null;
        if (uploadFileHandler == null) {
            uploadFileHandler = new DummyUploadHandler();
        }
        return uploadFileHandler;
    }

    @Override
    public List<Map<String, Object>> searchFiles(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        return null;
    }
}
