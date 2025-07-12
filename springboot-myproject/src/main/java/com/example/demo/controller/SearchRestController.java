package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.artworkdto.ArtworkCardDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ArtworkService;
import com.example.demo.service.TagService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class SearchRestController {
	private final UserService userService;

	private final ArtworkService artworkService;

	private final TagService tagService;

	// 搜尋用戶
	@GetMapping("/user")
	public ResponseEntity<ApiResponse<List<UserDto>>> searchUsers(@RequestParam String keyword) {
		List<UserDto> users = userService.searchByKeyword(keyword);
		return ResponseEntity.ok(ApiResponse.success("搜尋成功", users));
	}

	// 搜尋作品（可排序 驗證身分）
	@GetMapping("/artwork")
    public ResponseEntity<ApiResponse<Page<ArtworkCardDto>>> searchArtworks(
            @RequestParam String keyword,
            Pageable pageable,
            @RequestAttribute(name = "userCertDto", required = false) UserCertDto userCertDto
    ) {
        Integer viewerId = userCertDto != null ? userCertDto.getUserId() : null;
        Page<ArtworkCardDto> result = artworkService.searchByTitle(keyword, pageable, viewerId);
        return ResponseEntity.ok(ApiResponse.success("搜尋成功", result));
    }


	// 搜尋標籤
	@GetMapping("/tag")
    public ResponseEntity<ApiResponse<List<TagDto>>> searchTags(@RequestParam String keyword) {
        List<TagDto> tags = tagService.searchByName(keyword);
        return ResponseEntity.ok(ApiResponse.success("搜尋成功", tags));
    }
}
