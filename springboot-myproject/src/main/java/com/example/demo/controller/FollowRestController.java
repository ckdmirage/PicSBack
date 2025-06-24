package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.followDto.FollowCountDto;
import com.example.demo.model.dto.followDto.FollowDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.enums.FollowType;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.FollowService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class FollowRestController {

	private final FollowService followService;

	// 追蹤
	@PostMapping("/{targetUserId}")
	public ResponseEntity<ApiResponse<Boolean>> follow(@RequestAttribute UserCertDto userCertDto,
			@PathVariable Integer targetUserId) {
		followService.follow(userCertDto.getUserId(), targetUserId);
		return ResponseEntity.ok(ApiResponse.success("關注成功", true));
	}

	// 取消追蹤
	@DeleteMapping("/{targetUserId}")
	public ResponseEntity<ApiResponse<Boolean>> unfollow(
	    @RequestAttribute UserCertDto userCertDto,
	    @PathVariable Integer targetUserId
	) {
	    followService.unfollow(userCertDto.getUserId(), targetUserId);
	    return ResponseEntity.ok(ApiResponse.success("取消成功", false));
	}
	
	// 確認追蹤(前端渲染)
	@GetMapping("/hasfollowed")
	public ResponseEntity<ApiResponse<Boolean>> hasFollowed(@RequestAttribute UserCertDto userCertDto,
			@RequestParam Integer followingId) {
		Boolean hasFollowed = followService.hasFollowed(userCertDto.getUserId(), followingId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", hasFollowed));
	}

	// 查某人粉絲/追蹤數量
	@GetMapping("/count/{userId}")
	public ResponseEntity<ApiResponse<FollowCountDto>> getFollowCounts(@PathVariable Integer userId) {
		int followers = followService.countFollows(userId, FollowType.FOLLOWERS);
		int followings = followService.countFollows(userId, FollowType.FOLLOWINGS);
		FollowCountDto dto = new FollowCountDto(followers, followings);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", dto));
	}

	// 自己追蹤列表
	@GetMapping("/following")
	public ResponseEntity<ApiResponse<List<FollowDto>>> getMyFollowings(@RequestAttribute UserCertDto userCertDto) {
		List<FollowDto> result = followService.getFollowings(userCertDto.getUserId());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", result));
	}

	// 他人追蹤列表
	@GetMapping("/following/{userId}")
	public ResponseEntity<ApiResponse<List<FollowDto>>> getUserFollowings(@PathVariable Integer userId) {
		List<FollowDto> result = followService.getFollowings(userId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", result));
	}

	// 自己粉絲列表
	@GetMapping("/follower")
	public ResponseEntity<ApiResponse<List<FollowDto>>> getMyFollowers(@RequestAttribute UserCertDto userCertDto) {
		List<FollowDto> result = followService.getFollowers(userCertDto.getUserId());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", result));
	}

	// 他人粉絲列表
	@GetMapping("/follower/{userId}")
	public ResponseEntity<ApiResponse<List<FollowDto>>> getUserFollowers(@PathVariable Integer userId) {
		List<FollowDto> result = followService.getFollowers(userId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", result));
	}

}
