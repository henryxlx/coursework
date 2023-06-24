package org.edunext.coursework.kernel.service;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.util.JsonUtil;
import com.jetwinner.util.MapUtil;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import org.edunext.coursework.kernel.dao.UploadFileDao;
import org.edunext.coursework.kernel.dao.UploadFileShareDao;
import org.edunext.coursework.kernel.service.upload.DummyUploadHandler;
import org.edunext.coursework.kernel.service.upload.UploadFileHandlerAware;
import org.edunext.coursework.kernel.service.upload.UploadFileHandlerTypeEnum;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@Service
public class UploadFileServiceImpl implements UploadFileService {

    private final UploadFileShareDao uploadFileShareDao;
    private final UploadFileDao uploadFileDao;
    private final ApplicationContext applicationContext;

    public UploadFileServiceImpl(UploadFileShareDao uploadFileShareDao,
                                 UploadFileDao uploadFileDao,
                                 ApplicationContext applicationContext) {

        this.uploadFileShareDao = uploadFileShareDao;
        this.uploadFileDao = uploadFileDao;
        this.applicationContext = applicationContext;
    }

    @Override
    public int searchFileCount(Map<String, Object> conditions) {
        Object createdUserIds = null;
        if ("shared".equals(conditions.get("source"))) {
            //Find all the users who is sharing with current user.
            List<Map<String, Object>> myFriends = this.uploadFileShareDao.findMySharingContacts(conditions.get("currentUserId"));

            if (myFriends != null) {
                createdUserIds = EasyStringUtil.implode(",", ArrayToolkit.column(myFriends, "sourceUserId"));
            } else {
                //Browsing shared files, but nobody is sharing with current user.
                return 0;
            }

        } else if (EasyStringUtil.isNotBlank(conditions.get("currentUserId"))) {
            createdUserIds = conditions.get("currentUserId");
        }

        if (createdUserIds != null) {
            conditions.put("createdUserIds", createdUserIds);
        }

        return this.uploadFileDao.searchFileCount(conditions);
    }

    @Override
    public List<Map<String, Object>> searchFiles(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        OrderBy orderBy;
        switch (sort) {
            case "latestUpdated":
                orderBy = OrderBy.build(1).addDesc("updatedTime");
                break;
            case "oldestUpdated":
                orderBy = OrderBy.build(1).addAsc("updatedTime");
                break;
            case "latestCreated":
                orderBy = OrderBy.build(1).addDesc("createdTime");
                break;
            case "oldestCreated":
                orderBy = OrderBy.build(1).addAsc("createdTime");
                break;
            case "extAsc":
                orderBy = OrderBy.build(1).addAsc("ext");
                break;
            case "extDesc":
                orderBy = OrderBy.build(1).addDesc("ext");
                break;
            case "nameAsc":
                orderBy = OrderBy.build(1).addAsc("filename");
                break;
            case "nameDesc":
                orderBy = OrderBy.build(1).addDesc("filename");
                break;
            case "sizeAsc":
                orderBy = OrderBy.build(1).addAsc("size");
                break;
            case "sizeDesc":
                orderBy = OrderBy.build(1).addDesc("size");
                break;
            default:
                throw new RuntimeException("参数sort不正确。");
        }

        Object createdUserIds = null;
        if ("shared".equals(conditions.get("source"))) {
            //Find all the users who is sharing with current user.
            List<Map<String, Object>> myFriends = this.uploadFileShareDao.findMySharingContacts(conditions.get("currentUserId"));

            if (myFriends != null) {
                createdUserIds = EasyStringUtil.implode(",", ArrayToolkit.column(myFriends, "sourceUserId"));
            } else {
                //Browsing shared files, but nobody is sharing with current user.
                return new ArrayList<>(0);
            }

        } else if (EasyStringUtil.isNotBlank(conditions.get("currentUserId"))) {
            createdUserIds = conditions.get("currentUserId");
        }

        if (createdUserIds != null) {
            conditions.put("createdUserIds", createdUserIds);
        }

        return this.uploadFileDao.searchFiles(conditions, orderBy, start, limit);
    }


    @Override
    public Map<String, Object> addFile(String targetType, Integer targetId, Map<String, Object> fileInfo,
                                       String uploadFileHandlerType, MultipartFile originalFile) {

        Map<String, Object> file = this.getUploadFileHandler(uploadFileHandlerType)
                .addFile(targetType, targetId, fileInfo, originalFile);

        return this.uploadFileDao.addFile(file);
    }

    @Override
    public Map<String, Object> makeUploadParams(Map<String, Object> params) {
        return getUploadFileHandler().makeUploadParams(params);
    }

    @Override
    public void deleteFiles(Set<Integer> fileIds) {
        fileIds.forEach(id -> this.deleteFile(id));
    }

    @Override
    public Map<String, Object> getFile(Object fileId) {
        Map<String, Object> file = this.uploadFileDao.getFile(fileId);
        if (MapUtil.isEmpty(file)) {
            return null;
        }
        return this.getFileImplementorByFile(file).getFile(file);
    }

    @Override
    public void increaseFileUsedCount(Integer... fileIds) {
        this.uploadFileDao.updateFileUsedCount(fileIds, 1);
    }

    @Override
    public void decreaseFileUsedCount(Integer... fileIds) {
        this.uploadFileDao.updateFileUsedCount(fileIds, -1);
    }

    public void deleteFile(Integer id) {
        Map<String, Object> file = this.getFile(id);
        if (MapUtil.isEmpty(file)) {
            throw new RuntimeException("文件(#" + id + ")不存在，删除失败");
        }

        boolean deleteSubFile = true;
        if (EasyStringUtil.isNotBlank(file.get("etag"))) {
            int etagCount = this.uploadFileDao.findFilesCountByEtag(file.get("etag"));
            if (etagCount > 1) {
                deleteSubFile = false;
            }
        }

        if (EasyStringUtil.isNotBlank(file.get("convertParams"))) {
            Map<String, Object> convertParamsMap = JsonUtil.jsonDecodeMap(file.get("convertParams"));
            if ("HLSEncryptedVideo".equals(convertParamsMap.get("convertor")) ||
                    "ppt".equals(convertParamsMap.get("convertor"))) {

                deleteSubFile = true;
            }
        }

        this.getFileImplementorByFile(file).deleteFile(file, deleteSubFile);

        this.uploadFileDao.deleteFile(id);
    }

    private UploadFileHandlerAware getFileImplementorByFile(Map<String, Object> file) {
        return this.getUploadFileHandler(String.valueOf(file.get("storage")));
    }

    private UploadFileHandlerAware getUploadFileHandler() {
        return getUploadFileHandler(UploadFileHandlerTypeEnum.LOCAL);
    }

    private UploadFileHandlerAware getUploadFileHandler(String type) {
        type = type != null ? type.trim().toLowerCase() : type;
        if ("local".equals(type)) {
            return getUploadFileHandler(UploadFileHandlerTypeEnum.LOCAL);
        } else if ("cloud".equals(type)) {
            return getUploadFileHandler(UploadFileHandlerTypeEnum.CLOUD);
        } else {
            return getUploadFileHandler(UploadFileHandlerTypeEnum.LOCAL);
        }
    }

    private UploadFileHandlerAware getUploadFileHandler(UploadFileHandlerTypeEnum uploadFileHandlerType) {
        UploadFileHandlerAware uploadFileHandler = this.applicationContext != null ?
                this.applicationContext.getBean(uploadFileHandlerType.getName(), UploadFileHandlerAware.class) : null;
        if (uploadFileHandler == null) {
            uploadFileHandler = new DummyUploadHandler();
        }
        return uploadFileHandler;
    }
}
