package com.shourov.springUploadAndDownload.controller;

import com.shourov.springUploadAndDownload.enums.FileMessage;
import com.shourov.springUploadAndDownload.service.FileService;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@RestController
@RequestMapping("/file")
public class FileController {
    
    private static final Logger logger = Logger.getLogger(FileController.class.getName());
    private final FileService service;
    private final MessageSource messageSource;
    
    public FileController(FileService service, MessageSource source) {
        this.service = service;
        this.messageSource = source;
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<?>> getAllFiles() {
        return ResponseEntity.ok(service.getAllFileNames());
    }
    
    @GetMapping(
            value = "/download/{name}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<?> downloadFile(@PathVariable(value = "name") String fileName) {
        Resource file = service.downloadFile(fileName, Locale.ENGLISH);
        if(file == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
        }
    }
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam String fileName,
            @RequestParam(name = "file") MultipartFile file) {
        
        Locale locale = Locale.forLanguageTag("bn");
        String status = service.uploadFile(fileName, file, locale);
        String successMessage = messageSource.getMessage(FileMessage.FILE_UPLOAD_SUCCESS.getCode(), null, locale);
        String existsMessage = messageSource.getMessage(FileMessage.FILE_ALREADY_EXISTS.getCode(), null, locale);
        logger.info(status);
        return status.equals(successMessage)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : status.equals(existsMessage)
                    ? ResponseEntity.status(HttpStatus.NOT_MODIFIED).build()
                    : ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }
}
