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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.service.FavouriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favourite")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class FavouriteRestController {

	public final FavouriteService favouriteService;
	
	// 收藏作品
    @PostMapping("/{artworkId}")
    public ResponseEntity<?> addFavourite(@PathVariable Integer artworkId,
                                          @RequestAttribute UserCertDto userCertDto) {
        Integer userId = userCertDto.getUserId(); 
        favouriteService.addFavourite(userId, artworkId);
        return ResponseEntity.ok("收藏成功");
    }

    // 取消收藏
    @DeleteMapping("/{artworkId}")
    public ResponseEntity<?> removeFavourite(@PathVariable Integer artworkId,
                                             @RequestAttribute UserCertDto userCertDto) {
        Integer userId = userCertDto.getUserId();
        favouriteService.removeFavourite(userId, artworkId);
        return ResponseEntity.ok("取消收藏成功");
    }

    // 檢查是否收藏
    @GetMapping("/{artworkId}")
    public ResponseEntity<Boolean> hasFavourited(@PathVariable Integer artworkId,
                                                 @RequestAttribute UserCertDto userCertDto) {
        Integer userId = userCertDto.getUserId();
        boolean has = favouriteService.hasFavourited(userId, artworkId);
        return ResponseEntity.ok(has);
    }

    // 取得收藏清單
    @GetMapping("/my")
    public ResponseEntity<List<ArtworkDisplayDto>> getMyFavourites(
            @RequestAttribute UserCertDto userCertDto) {
        Integer userId = userCertDto.getUserId();
        List<ArtworkDisplayDto> result = favouriteService.getMyFavourites(userId);
        return ResponseEntity.ok(result);
    }
	
	
}
