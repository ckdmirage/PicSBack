package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.NotificationDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.NotificationService;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class NotificationRestController {

	@Autowired
	private NotificationService notificationService;

	// 用戶查詢通知列表
	@GetMapping
	public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications(
			@RequestAttribute UserCertDto userCertDto) {
		List<NotificationDto> notifications = notificationService.getNotificationsForUser(userCertDto.getUserId());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", notifications));
	}

	// 用戶標記通知為已讀
	@PostMapping("/{id}/read")
	public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long id) {
		notificationService.markAsRead(id);
		return ResponseEntity.ok(ApiResponse.success("通知已標記為已讀", null));
	}
	
	@GetMapping("/unread")
	public ResponseEntity<ApiResponse<Boolean>> hasUnreadNotifications(
	        @RequestAttribute UserCertDto userCertDto) {
	    boolean hasUnread = notificationService.hasUnread(userCertDto.getUserId());
	    return ResponseEntity.ok(ApiResponse.success("查詢成功", hasUnread));
	}

}
