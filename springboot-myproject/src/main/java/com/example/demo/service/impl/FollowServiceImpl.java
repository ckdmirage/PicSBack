package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FollowService;

@Service
public class FollowServiceImpl implements FollowService {
	
	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void follow(Integer followerId, Integer followingId) {
		if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("不能追蹤自己");
        }

	}

	@Override
	public void unfollow(Integer followerId, Integer followingId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<UserDto> getFollowings(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDto> getFollowers(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasFollowed(Integer followerId, Integer followingId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int countFollowings(Integer userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countFollowers(Integer userId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
