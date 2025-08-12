package com.shourov.springUploadAndDownload.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

public interface FileService {
    List<String> getAllFileNames();
    String uploadFile(String fileName, MultipartFile file, Locale locale);
    Resource downloadFile(String fileName, Locale locale);
}
