package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.FollowException;
import com.example.demo.exception.UserNoFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.followDto.FollowDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.entity.Follow;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.serializable.FollowId;
import com.example.demo.model.enums.FollowType;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FollowService;

import jakarta.transaction.Transactional;

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
	public List<FollowDto> getFollowings(Integer userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNoFoundException("用戶不存在"));
		List<Follow> followings = followRepository.findByFollower(user);
		return followings.stream().map(follow -> {
			User u = follow.getFollowing();
			return new FollowDto(userMapper.toDto(u), follow.getCreatedAt());
		}).toList();
	}

	// 粉絲
	@Override
	public List<FollowDto> getFollowers(Integer userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNoFoundException("用戶不存在"));
		List<Follow> followeds = followRepository.findByFollowing(user);
		return followeds.stream().map(follow -> {
			User u = follow.getFollower();
			return new FollowDto(userMapper.toDto(u), follow.getCreatedAt());
		}).toList();
	}

	@Override
	public Boolean hasFollowed(Integer followerId, Integer followingId) {
		return followRepository.existsById(new FollowId(followerId, followingId));
	}

	/*
	 * @Override public Integer countFollowings(Integer userId) { User user =
	 * userRepository.findById(userId).orElseThrow(() -> new
	 * RuntimeException("用戶不存在")); return followRepository.countByFollower(user); }
	 * 
	 * @Override public Integer countFollowers(Integer userId) { User user =
	 * userRepository.findById(userId).orElseThrow(() -> new
	 * RuntimeException("用戶不存在")); return followRepository.countByFollowing(user); }
	 */

	// 粉絲/追蹤數
	public Integer countFollows(Integer userId, FollowType type) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用戶不存在"));

		return switch (type) {
		case FOLLOWERS -> followRepository.countByFollower(user); // 有誰追蹤他 = 粉絲數
		case FOLLOWINGS -> followRepository.countByFollowing(user); // 他追蹤誰 = 關注數
		};
	}
}
