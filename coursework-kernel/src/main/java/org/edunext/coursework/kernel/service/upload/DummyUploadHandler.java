package org.edunext.coursework.kernel.service.upload;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xulixin
 */
public class DummyUploadHandler implements UploadFileHandlerAware {

    @Override
    public Map<String, Object> makeUploadParams(Map<String, Object> params) {
        return new HashMap<>(0);
    }

    @Override
    public Map<String, Object> addFile(String targetType, Integer targetId, Map fileInfo, MultipartFile originalFile) {
        return new HashMap<>(0);
    }

    @Override
    public Map<String, Object> getFile(Map<String, Object> file) {
        return new HashMap<>(0);
    }

    @Override
    public void deleteFile(Map<String, Object> file, boolean deleteSubFile) {

    }
}
