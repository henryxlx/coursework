package org.edunext.coursework.kernel.service.upload;

import org.edunext.coursework.kernel.service.UploadFileService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyUploadFileService implements UploadFileService {

    @Override
    public ApplicationContext getApplicationContext() {
        return null;
    }

    @Override
    public Map<String, Object> makeUploadParams(Map<String, Object> params) {
        return new HashMap<>(0);
    }

    @Override
    public Map<String, Object> addFile(String targetType, Integer targetId, Map fileInfo, MultipartFile originalFile) {
        return new HashMap<>(0);
    }

    @Override
    public List<Map<String, Object>> searchFiles(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        return new ArrayList<>(0);
    }
}
