package com.shourov.springUploadAndDownload.enums;

public enum FileMessage {
    FILE_ALREADY_EXISTS("file.exists"),
    FILE_UPLOAD_SUCCESS("file.upload.success"),
    FILE_UPLOAD_FAILURE("file.upload.failure");
    
    private final String code;
    
    FileMessage(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
}
