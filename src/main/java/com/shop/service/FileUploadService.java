package com.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {

    private final String uploadDir = "/Users/hyeranpakr/Downloads/demo/src/main/resources/static/files/";

    public void uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + uploadDir);
            }
        }

        String fileName = file.getOriginalFilename();
        File destFile = new File(uploadDir + fileName);

        try {
            FileCopyUtils.copy(file.getBytes(), destFile);
        } catch (IOException e) {
            throw new IOException("Failed to upload file: " + fileName, e);
        }
    }
}
