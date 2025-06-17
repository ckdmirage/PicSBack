package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.follow.FollowDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.enums.FollowType;

public interface FollowService {

	// 1. 追蹤
	void follow(Integer followerId, Integer followingId);

	// 2. 取消追蹤
	void unfollow(Integer followerId, Integer followingId);

	// 3. 查詢我追蹤的人
	List<FollowDto> getFollowings(Integer userId);

	// 4. 查詢我的粉絲
	List<FollowDto> getFollowers(Integer userId);

	// 5. 查詢是否已追蹤
	Boolean hasFollowed(Integer followerId, Integer followingId);

	// 6. 查詢追蹤/粉絲數
	//Integer countFollowings(Integer userId);

	//Integer countFollowers(Integer userId);
	
	Integer countFollows(Integer userId, FollowType type);
}
