package com.example.demo.model.dto.followDto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 建立追蹤關係用Dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto {
	private Integer userId;
	private String username;
	private String avatarUrl;
	private String email;
	private String role;
	private LocalDateTime created;
	private LocalDateTime followedAt;
}
