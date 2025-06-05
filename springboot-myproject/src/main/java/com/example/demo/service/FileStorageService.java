package com.example.demo.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
	String storeFile(MultipartFile file) throws IOException;
}
