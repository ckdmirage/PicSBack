package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.UserNoFoundException;
import com.example.demo.repository.LikesRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.LikesService;
import com.example.demo.util.JwtUtil;

@RestController
@RequestMapping("/like")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class LikesController {
	@Autowired
	private LikesService likesService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/{artworkId}")
	public ResponseEntity<ApiResponse<String>> addLike(@RequestHeader("Authorization") String token, @PathVariable Integer artworkId){
		String username = jwtUtil.extractUsername(token);
		likesService.likeArtwork(1, artworkId);
		return null;
	}
}
