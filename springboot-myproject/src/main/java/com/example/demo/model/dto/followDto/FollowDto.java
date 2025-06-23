package com.example.demo.model.dto.followDto;

import java.time.LocalDateTime;

import com.example.demo.model.dto.userdto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto {
	private Integer userId;
	private String username;
	private String email;
	private String role;
	private LocalDateTime created;
	private LocalDateTime followedAt;
}
