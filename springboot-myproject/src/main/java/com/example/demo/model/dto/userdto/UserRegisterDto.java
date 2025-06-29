package com.example.demo.model.dto.userdto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

//註冊用dto,前端傳回資料
@Data
public class UserRegisterDto {
	
	@Size(min = 6, message = "密碼至少 6 碼")
	private String password;

	@NotBlank(message = "用戶名不能為空")
	private String username;

	@Email(message = "請輸入正確的 email 格式")
	private String email;

	private LocalDateTime created;

	private String role;
}
