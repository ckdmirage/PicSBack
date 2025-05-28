package com.example.demo.model.dto.userdto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
	
	@NotBlank(message = "用戶名不能為空")
	private String username;
	
	@NotBlank(message = "密碼不能為空")
	private String password;
	
}
