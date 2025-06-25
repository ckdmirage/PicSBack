package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.PasswordChangeDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserLoginDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;
import com.example.demo.model.dto.userdto.UserUpdateNameDto;

public interface UserService {
	UserCertDto login(UserLoginDto userLoginDto);
	
	UserDto getUserDto(Integer userId);
	
	List<UserDto> searchByKeyword(String keyword);
	
	
	void addUser(UserRegisterDto userRegisterDto);
	
	boolean verifyUserRegisterToken(String token);
	
	
	String updateAvatar(MultipartFile file, UserCertDto userCertDto);
	
	UserCertDto updateName(Integer userId, UserUpdateNameDto dto);
	
	void requestEmailChange(Integer userId, String newEmail);
	
	void verifyEmailChange(String token);
	
	void requestPasswordChange(Integer userId, PasswordChangeDto passwordChangeDto);
	
	void verifyPasswordChange(String token);
}
