package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserLoginDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;

public interface UserService {
	UserCertDto login(UserLoginDto userLoginDto);

	void addUser(UserRegisterDto userRegisterDto);

	UserDto getUserDto(Integer userId);//導向個人主頁

	boolean verifyUserToken(String token);
	
	List<UserDto> searchByKeyword(String keyword);
}
