package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.TagException;
import com.example.demo.exception.UnLoginException;
import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.tagdto.TagCreateDto;
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
	
	//作品上傳需要驗證用戶登入的身分
	@PostMapping("upload")
	public ResponseEntity<ApiResponse<Artwork>> uploadArtwork(@RequestBody ArtworkUploadDto artworkUploadDto,
			@RequestAttribute UserCertDto userCertDto) throws UnLoginException {
		Artwork artwork = artworkService.uploadArtwork(userCertDto, artworkUploadDto);
		return ResponseEntity.ok(ApiResponse.success("上傳成功!", artwork));
	}
	
	//瀏覽作品
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ArtworkDisplayDto>> ArtworkDisplay(@PathVariable Integer id){
		ArtworkDisplayDto artworkDisplayDto = artworkService.getArtworkDisplayDtoById(id);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", artworkDisplayDto));
	}
	

	
	
	@PostMapping("/tag/add")
	public ResponseEntity<ApiResponse<Tag>> addTag(@RequestBody TagCreateDto tagCreateDto) throws TagException {
		Tag tag = tagService.addTag(tagCreateDto);
		return ResponseEntity.ok(ApiResponse.success("新增成功", tag));
	}
	
	@GetMapping("/tag/search")
	public ResponseEntity<ApiResponse<List<Tag>>> searchTags (@RequestParam ("keyword") String keyword){
		return ResponseEntity.ok(ApiResponse.success("查詢成功", tagService.searchTags(keyword)));
	}
}
