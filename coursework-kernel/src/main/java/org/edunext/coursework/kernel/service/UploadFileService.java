package org.edunext.coursework.kernel.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
public interface UploadFileService {

    int searchFileCount(Map<String, Object> conditions);

    List<Map<String, Object>> searchFiles(Map<String, Object> conditions, String sort, Integer start, Integer limit);


    Map<String, Object> addFile(String targetType, Integer targetId, Map<String, Object> fileInfo,
                                String uploadFileHandlerType, MultipartFile originalFile);

    Map<String, Object> makeUploadParams(Map<String, Object> params);

    void deleteFiles(Set<Integer> fileIds);
}
