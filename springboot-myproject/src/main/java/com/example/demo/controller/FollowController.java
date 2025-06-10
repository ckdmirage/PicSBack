package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.demo.model.dto.FollowDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.FollowService;

@RestController
@RequestMapping("/api/follow")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class FollowController {
	@Autowired
	private FollowService followService;

	@PostMapping("/{followerId}/{followingId}")
	public ResponseEntity<ApiResponse<String>> follow(@PathVariable Integer followerId,
			@PathVariable Integer followingId) {
		followService.follow(followerId, followingId);
		return ResponseEntity.ok(ApiResponse.success("關注成功", null));
	}

	@DeleteMapping("/{followerId}/{followingId}")
	public ResponseEntity<ApiResponse<String>> unfollow(@PathVariable Integer followerId,
			@PathVariable Integer followingId) {
		followService.unfollow(followerId, followingId);
		return ResponseEntity.ok(ApiResponse.success("取消關注成功", null));
	}
	
	//追蹤列表
	@GetMapping("/following")
	public ResponseEntity<ApiResponse<List<FollowDto>>> getfollowings(@RequestAttribute UserCertDto userCertDto){
		
		List<FollowDto> followings = followService.getFollowings(userCertDto.getUserId());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", followings));
	}
	
	//粉絲列表
	@GetMapping("/follower")
	public ResponseEntity<ApiResponse<List<FollowDto>>> getFollowers(@RequestAttribute UserCertDto userCertDto) {
		List<FollowDto> followers = followService.getFollowers(userCertDto.getUserId());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", followers));
	}
	
	//確認追蹤
	@GetMapping("/hasfollowed")
	public ResponseEntity<ApiResponse<Boolean>> hasFollowed(@RequestParam Integer followerId, @RequestParam Integer followingId){
		Boolean hasFollowed = followService.hasFollowed(followerId, followingId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", hasFollowed));
	}
	
	//追蹤人數
	@GetMapping("/following/count")
	public ResponseEntity<ApiResponse<Integer>> countFollowing(@RequestAttribute UserCertDto userCertDto){
		Integer count = followService.countFollowings(userCertDto.getUserId());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", count));
	}
	
	//粉絲數
	@GetMapping("/follower/count")
	public ResponseEntity<ApiResponse<Integer>> countFollower(@RequestAttribute UserCertDto userCertDto){
		Integer count = followService.countFollowers(userCertDto.getUserId());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", count));
	}
}
