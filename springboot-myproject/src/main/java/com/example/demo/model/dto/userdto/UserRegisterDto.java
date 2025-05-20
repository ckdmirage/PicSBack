package com.example.demo.model.dto.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//註冊登陸功能用dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
	@NotBlank
	private String username;
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String password;
}
