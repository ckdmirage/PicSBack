	package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.VerificationToken;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;

public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
    private EmailService emailService;
	
	
	@Autowired
	private UserMapper userMapper;

	// 密碼加密
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void addUser(UserRegisterDto userRegisterDto) {
		// 用戶保存
		User user = saveUser(userRegisterDto);

		// 產token驗證與發送

	}

	// 子方法用戶保存
	private User saveUser(UserRegisterDto userRegisterDto) {
		String passwordHash = passwordEncoder.encode(userRegisterDto.getPassword());

		User user = new User(null, userRegisterDto.getUsername(), userRegisterDto.getEmail(), passwordHash, false,
				userRegisterDto.getCreated(), userRegisterDto.getRole(), new ArrayList<>(), new ArrayList<>());
		return userRepository.save(user); // 這樣就含有id了
	}

	// 子方法token驗證與發送
	private void createAndSendVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken(null, token, user,
				LocalDateTime.now().plusHours(24));
		verificationTokenRepository.save(verificationToken);
		
		//
		String verifyLink = "http://localhost:8082/user/verify?token=" + token;
		emailService.sendEmail(user.getEmail(), "請驗證您的帳號",
                "請點擊以下連結完成驗證：\n" + verifyLink);
	}

	@Override
	public UserDto getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
