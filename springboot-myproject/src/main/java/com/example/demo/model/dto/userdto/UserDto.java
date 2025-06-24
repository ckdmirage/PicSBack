package com.example.demo.model.dto.userdto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//前端顯示用dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private Integer id;
	
	private String username;
	
	private String email;
	
	private LocalDateTime created;

	private String avatarUrl;
	
}
