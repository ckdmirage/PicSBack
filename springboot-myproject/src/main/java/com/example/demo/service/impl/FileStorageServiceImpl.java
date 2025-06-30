package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {
	// D:/myprojectImg/ E:/HTMLCSSJavaScript/myprojectImg/
    private final String artworkDir = "E:/HTMLCSSJavaScript/myprojectImg/artwork/";
    private final String avatarDir = "E:/HTMLCSSJavaScript/myprojectImg/avatar/";

    @Override
    public String storeFile(MultipartFile file, String type) throws IOException {
        String uploadDir;
        String resourcePath;

        switch (type) {
            case "avatar" -> {
                uploadDir = avatarDir;
                resourcePath = "/myprojectImg/avatar/";
            }
            case "artwork" -> {
                uploadDir = artworkDir;
                resourcePath = "/myprojectImg/artwork/";
            }
            default -> throw new IllegalArgumentException("未知類型: " + type);
        }

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(dir, filename);
        file.transferTo(dest);

        return "http://localhost:8081" + resourcePath + filename;
    }
}

