package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.UnLoginException;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ArtworkService;

@RestController
@RequestMapping("/artwork")
public class ArtworkRestController {
	@Autowired
	private ArtworkService artworkService;

	@PostMapping("upload")
	public ResponseEntity<ApiResponse<Artwork>> uploadArtwork(@RequestBody ArtworkUploadDto artworkUploadDto,
			@RequestAttribute UserCertDto userCertDto) throws UnLoginException {
		Artwork artwork = artworkService.uploadArtwork(userCertDto, artworkUploadDto);
		return ResponseEntity.ok(ApiResponse.success("上傳成功!", artwork));
	}
	
	
	
}
