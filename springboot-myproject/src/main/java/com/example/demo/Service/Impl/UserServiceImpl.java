package com.example.demo.Service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.Service.UserService;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;
	
	//密碼加密
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDto getUser(String username) {
		// TODO Auto-generated method stub
		User user = userRepository.getUser(username);
		if (user == null) {
			return null;
		}
		return userMapper.toDto(user);
	}

	@Override
	public void addUser(UserRegisterDto userRegisterDto) {
		// 密碼加密
		//String passwordHash = passwordEncoder.encode(userDto.);
		
		//User user = new User(null, username, passwordHash, email, verified, created, role, new ArrayList<>(), new ArrayList<>());
	}

}
