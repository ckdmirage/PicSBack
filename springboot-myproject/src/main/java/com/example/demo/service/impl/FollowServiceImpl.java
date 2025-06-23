package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.FollowException;
import com.example.demo.exception.UserNoFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.followDto.FollowDto;
import com.example.demo.model.entity.Follow;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.serializable.FollowId;
import com.example.demo.model.enums.FollowType;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FollowService;

@Service
public class FollowServiceImpl implements FollowService {

	@Autowired
	private FollowRepository followRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional
	public void follow(Integer followerId, Integer followingId) {
		if (followerId.equals(followingId)) {
			throw new FollowException("不能追蹤自己");
		}
		FollowId followId = new FollowId(followerId, followingId);
		if (followRepository.existsById(followId)) {
			throw new FollowException("帳號已追蹤");
		}
		User follower = userRepository.findById(followerId).orElseThrow(() -> new UserNoFoundException("追蹤者不存在"));
		User following = userRepository.findById(followingId).orElseThrow(() -> new UserNoFoundException("被追蹤者不存在"));
		Follow follow = new Follow(followId, follower, following, LocalDateTime.now());
		followRepository.save(follow);
	}

	@Override
	@Transactional
	public void unfollow(Integer followerId, Integer followingId) {
		FollowId followId = new FollowId(followerId, followingId);
		if (followRepository.existsById(followId)) {
			followRepository.deleteById(followId);
		}
	}

	// 追蹤者
	@Override
	@Transactional(readOnly = true)
	public List<FollowDto> getFollowings(Integer userId) {
		return followRepository.fetchFollowingsDto(userId);
	}	

	// 粉絲
	@Override
	@Transactional(readOnly = true)
	public List<FollowDto> getFollowers(Integer userId) {
		// 不需要查 user 本身了，直接查 DTO
		return followRepository.fetchFollowersDto(userId);
	}

	@Override
	public Boolean hasFollowed(Integer followerId, Integer followingId) {
		return followRepository.existsById(new FollowId(followerId, followingId));
	}

	// 粉絲/追蹤數
	public Integer countFollows(Integer userId, FollowType type) {
	    return switch (type) {
	        case FOLLOWERS -> followRepository.countByFollowerId(userId);
	        case FOLLOWINGS -> followRepository.countByFollowingId(userId);
	    };
	}


}
