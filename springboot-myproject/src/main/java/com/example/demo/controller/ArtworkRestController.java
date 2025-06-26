package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.TagException;
import com.example.demo.exception.UnLoginException;
import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.artworkdto.ArtworkCardDto;
import com.example.demo.model.dto.artworkdto.ArtworkDetailDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Tag;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ArtworkService;
import com.example.demo.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/artwork")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class ArtworkRestController {

	private final ArtworkService artworkService;

	private final TagService tagService;

	// 作品上傳(需驗證身分)
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse<ArtworkDetailDto>> uploadArtwork(
			@RequestPart("artwork") ArtworkUploadDto artworkUploadDto, @RequestPart("file") MultipartFile file,
			@RequestAttribute UserCertDto userCertDto) {
		ArtworkDetailDto dto = artworkService.uploadArtwork(userCertDto, artworkUploadDto, file);
		return ResponseEntity.ok(ApiResponse.success("上傳成功!", dto));
	}

	// 瀏覽作品(可以驗證身分 確認點讚)
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ArtworkDetailDto>> artworkDisplay(@PathVariable Integer id,
			@RequestAttribute(value = "userCertDto", required = false) UserCertDto userCertDto) {
		Integer currentUserId = userCertDto != null ? userCertDto.getUserId() : null;
		ArtworkDetailDto dto = artworkService.getArtworkDetailDto(id, currentUserId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", dto));
	}

	// 獲取所有作品(主頁)(可以驗證身分 確認點讚)
	@GetMapping
	public ResponseEntity<ApiResponse<List<ArtworkCardDto>>> getAllSorted(
			@RequestParam(defaultValue = "newest") String sort,
			@RequestAttribute(name = "userCertDto", required = false) UserCertDto userCertDto) {
		Integer viewerId = userCertDto != null ? userCertDto.getUserId() : null;
		return ResponseEntity.ok(ApiResponse.success("查詢成功", artworkService.getAllArtworkDtosSorted(sort, viewerId)));
	}

	// 根據作者顯示作品(作者頁面)(可以驗證身分 確認點讚)
	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<List<ArtworkCardDto>>> getByUserSorted(@PathVariable Integer userId,
			@RequestParam(defaultValue = "newest") String sort,
			@RequestAttribute(name = "userCertDto", required = false) UserCertDto userCertDto) {
		Integer viewerId = userCertDto != null ? userCertDto.getUserId() : null;
		return ResponseEntity
				.ok(ApiResponse.success("查詢成功", artworkService.getArtworkDtosByUserSorted(userId, sort, viewerId)));
	}

	// 根據標籤顯示作品 (標籤頁面)(可以驗證身分 確認點讚)
	@GetMapping("/tag/{tagname}")
	public ResponseEntity<ApiResponse<List<ArtworkCardDto>>> getByTagSorted(@PathVariable String tagname,
			@RequestParam(defaultValue = "newest") String sort,
			@RequestAttribute(name = "userCertDto", required = false) UserCertDto userCertDto) {
		Integer viewerId = userCertDto != null ? userCertDto.getUserId() : null;
		return ResponseEntity
				.ok(ApiResponse.success("查詢成功", artworkService.getArtworkDtosByTagSorted(tagname, sort, viewerId)));
	}

	// 刪除作品 
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteArtwork(@PathVariable Integer id,
			@RequestAttribute(name = "userCertDto", required = false) UserCertDto userCertDto) {
		 System.out.println("👤 auth = " + SecurityContextHolder.getContext().getAuthentication());
		if (userCertDto == null) {
			throw new UnLoginException("未登入，無法刪除作品");
		}

		artworkService.deleteArtwork(id, userCertDto);
		return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
	}

	// 新增標籤
	@PostMapping("/tag/add")
	public ResponseEntity<ApiResponse<Tag>> addTag(@RequestBody TagDto tagCreateDto) throws TagException {
		Tag tag = tagService.addTag(tagCreateDto);
		return ResponseEntity.ok(ApiResponse.success("新增成功", tag));
	}

	// 搜索標籤
	@GetMapping("/tag/search")
	public ResponseEntity<ApiResponse<List<TagDto>>> searchTags(@RequestParam("keyword") String keyword) {
		return ResponseEntity.ok(ApiResponse.success("查詢成功", tagService.searchTags(keyword)));
	}

}
