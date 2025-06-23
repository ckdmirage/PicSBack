package com.example.demo.mapper;


import org.springframework.stereotype.Component;

import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.entity.User;

@Component
public class UserMapper {
	public UserDto toDto(User user) {
		return new UserDto(
			user.getId(),
			user.getUsername(),
			user.getEmail(),
			user.getCreated(),
			user.getRole(),
			user.getAvatarUrl()
		);
	}
}
