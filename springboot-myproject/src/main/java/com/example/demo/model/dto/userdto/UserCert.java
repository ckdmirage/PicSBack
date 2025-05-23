package com.example.demo.model.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

//使用者憑證
@AllArgsConstructor
@Getter
@ToString
public class UserCert {
	private Integer userId;
	
	private String username;
	
	private String role;
}
