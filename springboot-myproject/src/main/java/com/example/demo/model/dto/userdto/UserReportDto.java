package com.example.demo.model.dto.userdto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//回傳前端的時候用
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReportDto {
	private Integer id;
	private String username;
	private String email;
	private Boolean emailVerified;
	private LocalDateTime userCreatedAt;
}
