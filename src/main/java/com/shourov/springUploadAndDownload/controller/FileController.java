package com.shourov.springUploadAndDownload.controller;

import com.shourov.springUploadAndDownload.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/file")
public class FileController {
    
    private final FileService service;
    
    public FileController(FileService service) {
        this.service = service;
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
        String status = service.uploadFile(fileName, file, Locale.ENGLISH);
        return "CREATED".equals(status)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : "EXIST".equals(status)
                    ? ResponseEntity.status(HttpStatus.NOT_MODIFIED).build()
                    : ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }
}
