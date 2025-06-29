package com.example.demo.model.dto.followDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 扁平dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowStatusDto {
	private Integer userId;
	private Boolean followed;
}
