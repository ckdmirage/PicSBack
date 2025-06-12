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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.TagException;
import com.example.demo.exception.UnLoginException;
import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.Tag;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ArtworkService;
import com.example.demo.service.TagService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/artwork")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class ArtworkRestController {
	@Autowired
	private ArtworkService artworkService;
	
	@Autowired
	private TagService tagService;
	
	//作品上傳(需要驗證用戶登入的身分)
	@PostMapping("upload")
	public ResponseEntity<ApiResponse<ArtworkDisplayDto>> uploadArtwork(@RequestBody ArtworkUploadDto artworkUploadDto,
			@RequestAttribute UserCertDto userCertDto) throws UnLoginException {
		ArtworkDisplayDto artworkDisplayDto =  artworkService.uploadArtwork(userCertDto, artworkUploadDto);
		return ResponseEntity.ok(ApiResponse.success("上傳成功!", artworkDisplayDto));
	}
	
	//瀏覽作品
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ArtworkDisplayDto>> artworkDisplay(@PathVariable Integer id){
		ArtworkDisplayDto artworkDisplayDto = artworkService.getArtworkDisplayDto(id);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", artworkDisplayDto));
	}
	
	//獲取所有作品(主頁)
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<ArtworkDisplayDto>>> getAllArtworks() {
	    List<ArtworkDisplayDto> artworks = artworkService.getAllArtworkDtos();
	    return ResponseEntity.ok(ApiResponse.success("查詢成功", artworks));
	}
	
	//根據作者顯示作品
	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<List<ArtworkDisplayDto>>> getArtworksByUser(@PathVariable Integer userId){
		return ResponseEntity.ok(ApiResponse.success("查詢成功", artworkService.getArtworkDtosByUser(userId)));
	}
	
	//刪除作品
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteArtwork(@PathVariable Integer id, @RequestHeader("Authorization") String token){
	    artworkService.deleteArtwork(id, token);
	    return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
	}
	
	//新增標籤
	@PostMapping("/tag/add")
	public ResponseEntity<ApiResponse<Tag>> addTag(@RequestBody TagDto tagCreateDto) throws TagException {
		Tag tag = tagService.addTag(tagCreateDto);
		return ResponseEntity.ok(ApiResponse.success("新增成功", tag));
	}
	
	//搜索標籤
	@GetMapping("/tag/search")
	public ResponseEntity<ApiResponse<List<Tag>>> searchTags (@RequestParam ("keyword") String keyword){
		return ResponseEntity.ok(ApiResponse.success("查詢成功", tagService.searchTags(keyword)));
	}
	
	//根據標籤顯示作品
	@GetMapping("/tag/{tagname}")
	public ResponseEntity<ApiResponse<List<ArtworkDisplayDto>>> getArtworksByTag(@PathVariable String tagname){
		return ResponseEntity.ok(ApiResponse.success("查詢成功", artworkService.getArtworkDtosByTag(tagname)));
	}
	
}
