package com.example.demo.model.dto;

import java.time.LocalDateTime;

import com.example.demo.model.enums.NotificationMessageType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class NotificationDto {
	private Integer id;
	private NotificationMessageType messageType;
	private LocalDateTime createdAt;
	private LocalDateTime readAt;
	private Integer adminId;

	public String getMessage() {
		return messageType.getMessage();
	}
}