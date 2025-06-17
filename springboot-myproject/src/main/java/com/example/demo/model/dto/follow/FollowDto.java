package com.example.demo.model.dto.follow;

import java.time.LocalDateTime;

import com.example.demo.model.dto.userdto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowDto {
	private UserDto userDto;
    private LocalDateTime followedAt;
}
