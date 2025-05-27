package com.example.demo.model.dto.userdto;

import java.time.LocalDateTime;

import lombok.Data;
//註冊用dto,前端傳回資料

@Data
public class UserRegisterDto {
	
	private String password;

	private String username;

	private String email;

	private LocalDateTime created;

	private String role;
}
