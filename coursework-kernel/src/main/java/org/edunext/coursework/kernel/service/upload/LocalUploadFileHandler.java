package org.edunext.coursework.kernel.service.upload;

import com.jetwinner.toolbag.FileToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.FastAppConst;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xulixin
 */
@Service("localUploadFileHandler")
public class LocalUploadFileHandler implements UploadFileHandlerAware {

    private final FastAppConst appConst;

    public LocalUploadFileHandler(FastAppConst appConst) {
        this.appConst = appConst;
    }

    @Override
    public Map<String, Object> makeUploadParams(Map<String, Object> params) {
        Map<String, Object> uploadParams = new HashMap<>(3);
        uploadParams.put("storage", "local");
        uploadParams.put("url", params.get("defaultUploadUrl"));

        Map<String, Object> postParams = new HashMap<>(1);
//        postParams.put("token", this.userService.makeToken("fileupload", params.get("user"), strtotime("+ 2 hours")));
        uploadParams.put("postParams", params);

        return uploadParams;
    }

    @Override
    public Map<String, Object> addFile(String targetType, Integer targetId, Map fileInfo, MultipartFile originalFile) {
        String fileExtensionName = FileToolkit.getFileExtension(originalFile.getOriginalFilename());
        boolean errors = FileToolkit.validateFileExtension(fileExtensionName);
        if (errors) {
            throw new RuntimeException("该文件格式，不允许上传。");
        }

        Map<String, Object> uploadFile = new HashMap<>(16);

        uploadFile.put("storage", "local");
        uploadFile.put("targetId", targetId);
        uploadFile.put("targetType", targetType);

        uploadFile.put("filename", originalFile.getOriginalFilename());

        uploadFile.put("ext", fileExtensionName);
        uploadFile.put("size", originalFile.getSize());

        String filename = FileToolkit.generateFilename(fileExtensionName);

        uploadFile.put("hashId", uploadFile.get("targetType") + "/" + uploadFile.get("targetId") + "/" + filename);

        uploadFile.put("convertHash", "ch-" + uploadFile.get("hashId"));
        uploadFile.put("convertStatus", "none");

        uploadFile.put("type", FileToolkit.getFileTypeByExtension(fileExtensionName));

        uploadFile.put("isPublic", EasyStringUtil.isBlank(fileInfo.get("isPublic")) ? 0 : 1);
        uploadFile.put("canDownload", EasyStringUtil.isBlank(uploadFile.get("canDownload")) ? 0 : 1);

        AppUser currentUser = AppUser.getCurrentUser(fileInfo);
        uploadFile.put("updatedUserId", currentUser.getId());
        uploadFile.put("createdUserId", currentUser.getId());
        uploadFile.put("updatedTime", System.currentTimeMillis());
        uploadFile.put("createdTime", System.currentTimeMillis());

        String targetPath = this.getFilePath(targetType, targetId, uploadFile.get("isPublic"));
        try {
            FileToolkit.transferFile(originalFile, targetPath, filename);
        } catch (IOException e) {
            throw new RuntimeException("本地文件上传失败！" + e.getMessage());
        }

        return uploadFile;
    }

    private String getFilePath(String targetType, Integer targetId, Object isPublic) {
        String baseDirectory;
        if (ValueParser.parseInt(isPublic) > 0) {
            baseDirectory = appConst.getUploadPublicDirectory();
        } else {
            baseDirectory = appConst.getDiskLocalDirectory();
        }
        return baseDirectory + "/" + targetType + "/" + targetId;
    }
}
