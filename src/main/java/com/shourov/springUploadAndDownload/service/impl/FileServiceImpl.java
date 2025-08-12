package com.shourov.springUploadAndDownload.service.impl;

import com.shourov.springUploadAndDownload.enums.FileMessage;
import com.shourov.springUploadAndDownload.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {
    
    private static final Logger log = Logger.getLogger(FileServiceImpl.class.getName());
    
    @Value("${rootPath}")
    private String rootPath;
    
    private final MessageSource messageSource;
    
    public FileServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public List<String> getAllFileNames() {
        File rootDir = new File(rootPath);
        File[] files = rootDir.listFiles();

        return files != null
                ? Arrays.stream(files).map(File::getName).collect(Collectors.toList())
                : null;
    }

    @Override
    public String uploadFile(String fileName, MultipartFile incomingFile, Locale locale) {
        
        if(fileName == null || fileName.trim().isEmpty()) {
            fileName = incomingFile.getOriginalFilename();
        }
        
        String absolutePath = rootPath + "/" + fileName;
        File file = new File(absolutePath);
        
        if(file.exists()) {
            return messageSource.getMessage(FileMessage.FILE_ALREADY_EXISTS.getCode(), null, locale);
        }

        Path filePath = Path.of(absolutePath);
        
        try {
            Files.copy(incomingFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return messageSource.getMessage(FileMessage.FILE_UPLOAD_SUCCESS.getCode(), null, locale);
        } catch (IOException e) {
            return messageSource.getMessage(FileMessage.FILE_UPLOAD_FAILURE.getCode(), null, locale);
        }
    }

    @Override
    public Resource downloadFile(String fileName, Locale locale) {
        String absolutePath = rootPath + "/" + fileName;
        File file = new File(absolutePath);
        
        if(file.exists()) {
            try {
                return new UrlResource(file.toURI());
            } catch (MalformedURLException e) {
                log.severe("File download failed.");
                return null;
            }
        }
        log.severe("File does not exist.");
        return null;
    }
}
