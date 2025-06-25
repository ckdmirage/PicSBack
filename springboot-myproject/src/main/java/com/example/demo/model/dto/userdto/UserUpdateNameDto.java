package com.example.demo.model.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//修改名字前端回傳dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateNameDto {
	private String username;
}
