package com.example.demo.service;

import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;

public interface UserService {
	public void addUser(UserRegisterDto userRegisterDto);

	public UserDto getUser(String username);
}
