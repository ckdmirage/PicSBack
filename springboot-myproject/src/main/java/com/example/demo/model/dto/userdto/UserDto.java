package com.example.demo.model.dto.userdto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDto {
	
	private Integer userId;
	
	private String username;
	
	private String email;
	
	private Boolean verified;
	
	private LocalDateTime created;
	
	private String role;
}
