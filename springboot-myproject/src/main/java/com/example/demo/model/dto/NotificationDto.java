package com.example.demo.model.dto;

import java.time.LocalDateTime;

import com.example.demo.model.enums.NotificationMessageType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // ✅ 必加這行
public class NotificationDto {
	private Integer id;
	private NotificationMessageType messageType;
	private LocalDateTime createdAt;
	private LocalDateTime readAt;
	private Integer adminId;

	// ✅ 這種非欄位對應的方法不影響 JPQL 建構
	public String getMessage() {
		return messageType.getMessage();
	}
}