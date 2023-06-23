package org.edunext.coursework.kernel.service.upload;

/**
 * @author xulixin
 */

public enum UploadFileHandlerTypeEnum {

    LOCAL("localUploadFileHandler"), CLOUD("cloudUploadFileHandler");

    private String name;

    UploadFileHandlerTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
