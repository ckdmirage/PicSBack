package com.example.demo.controller;

import java.util.List;
import java.util.Map;

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

import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.LikesService;
import com.example.demo.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class LikesRestController {
	private final LikesService likesService;

	private final JwtUtil jwtUtil;

	// 點讚
	@PostMapping("/{artworkId}")
	public ResponseEntity<ApiResponse<String>> addLike(@RequestAttribute UserCertDto userCertDto,
			@PathVariable Integer artworkId) {
		Integer userId = userCertDto.getUserId();
		likesService.likeArtwork(userId, artworkId);
		return ResponseEntity.ok(ApiResponse.success("點讚成功", null));
	}

	// 取消點讚
	@DeleteMapping("/{artworkId}")
	public ResponseEntity<ApiResponse<String>> unLike(@RequestAttribute UserCertDto userCertDto,
			@PathVariable Integer artworkId) {
		Integer userId = userCertDto.getUserId();
		likesService.unlikeArtwork(userId, artworkId);
		return ResponseEntity.ok(ApiResponse.success("取消成功", null));
	}

	// 查詢點讚數
	@GetMapping("/count/{artworkId}")
	public ResponseEntity<ApiResponse<Integer>> countLike(@PathVariable Integer artworkId) {
		Integer count = likesService.getLikeCount(artworkId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", count));
	}

	// 判斷是否點讚(前端渲染用)
	@GetMapping("/hasLiked/{artworkId}")
	public ResponseEntity<ApiResponse<Boolean>> hasLiked(@RequestAttribute UserCertDto userCertDto,
			@PathVariable Integer artworkId) {
		Integer userId = userCertDto.getUserId();
		Boolean hasLike = likesService.hasLiked(userId, artworkId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", hasLike));
	}

	// 批量查詢點讚數量
	@GetMapping("/counts")
	public ResponseEntity<ApiResponse<Map<Integer, Integer>>> countLikesBatch(
	        @RequestParam("artworkIds") List<Integer> artworkIds) {
	    Map<Integer, Integer> countMap = likesService.getLikesCountMap(artworkIds);
	    return ResponseEntity.ok(ApiResponse.success("批次查詢成功", countMap));
	}
	
	// 批量查詢是否點讚

}
