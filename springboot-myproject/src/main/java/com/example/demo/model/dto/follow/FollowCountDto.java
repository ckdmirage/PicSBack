package com.example.demo.model.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowCountDto {
    private int followerCount;
    private int followingCount;
}