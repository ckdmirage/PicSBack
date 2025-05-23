package com.example.demo.Service;

import java.time.LocalDateTime;

import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;

public interface UserService {
	public UserDto getUser(String username);
	public void addUser(UserRegisterDto userRegisterDto);
}
