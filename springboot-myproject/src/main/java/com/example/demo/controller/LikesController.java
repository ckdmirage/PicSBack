package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.LikesService;
import com.example.demo.util.JwtUtil;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/like")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class LikesController {
	@Autowired
	private LikesService likesService;
	@Autowired
	private JwtUtil jwtUtil;
	

	@PostMapping("/{artworkId}")
	public ResponseEntity<ApiResponse<String>> addLike(@RequestAttribute UserCertDto userCertDto, @PathVariable Integer artworkId){
		Integer userId = userCertDto.getUserId();
		likesService.likeArtwork(userId, artworkId);
		return ResponseEntity.ok(ApiResponse.success("點讚成功", null));
	}
	

	@DeleteMapping("/{artworkId}")
	public ResponseEntity<ApiResponse<String>> unLike(@RequestAttribute UserCertDto userCertDto, @PathVariable Integer artworkId){
		Integer userId = userCertDto.getUserId();
		likesService.unlikeArtwork(userId, artworkId);
		return ResponseEntity.ok(ApiResponse.success("取消成功", null));
	}
	
	@GetMapping("/count/{artworkId}")
	public ResponseEntity<ApiResponse<Integer>> countLike(@PathVariable Integer artworkId){
		Integer count = likesService.getLikeCount(artworkId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", count));
	}
	
	@GetMapping("/hasLiked/{artworkId}")
	public ResponseEntity<ApiResponse<Boolean>> hasLiked(@RequestAttribute UserCertDto userCertDto, @PathVariable Integer artworkId){
		Integer userId = userCertDto.getUserId();
		Boolean hasLike = likesService.hasLiked(userId, artworkId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", hasLike));
	}
}
