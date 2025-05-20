package com.example.demo.model.dto.userdto;

import jakarta.validation.constraints.NotBlank;

public class UserLoginDto {
	@NotBlank
	private String username;
	@NotBlank
	private String password;
}
