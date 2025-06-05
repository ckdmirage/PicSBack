package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService{

	private final String uploadDir = "E:/HTMLCSSJavaScript/myprojectImg/";
	
	@Override
	public String storeFile(MultipartFile file) throws IOException {
		File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(dir, filename);
        file.transferTo(dest);
        // 回傳給前端用於img的路徑（不加 http://localhost:8081，讓前端自己拼）
        String baseUrl = "http://localhost:8081";
        return baseUrl + "/myprojectImg/" + filename;
	}

}
