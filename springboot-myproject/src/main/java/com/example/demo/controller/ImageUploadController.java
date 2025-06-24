package com.example.demo.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.response.ApiResponse;
import com.example.demo.service.FileStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class ImageUploadController {

	private final FileStorageService fileStorageService;

	// 圖片上傳
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(@RequestParam("file") MultipartFile file)
			throws IOException {
		try {
			String url = fileStorageService.storeFile(file);
			Map<String, String> data = Collections.singletonMap("url", url);
			return ResponseEntity.ok(ApiResponse.success("上傳成功", data));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "檔案上傳失敗"));
		}
	}
}
