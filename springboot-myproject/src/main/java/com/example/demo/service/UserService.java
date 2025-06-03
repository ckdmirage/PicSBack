package com.example.demo.service;

import com.example.demo.exception.UserException;
import com.example.demo.exception.UserNoFoundException;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserLoginDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;

public interface UserService {
	public UserCertDto login(UserLoginDto userLoginDto);

	public void addUser(UserRegisterDto userRegisterDto);

	public UserDto getUserDto(String username);//導向個人主頁

	public boolean verifyUserToken(String token);
	
}
