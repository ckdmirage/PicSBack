package com.example.demo.model.dto.followDto;

import lombok.AllArgsConstructor;
import lombok.Data;

// 計數用扁平dto
@Data
@AllArgsConstructor
public class FollowCountDto {
    private int followerCount;
    private int followingCount;
}