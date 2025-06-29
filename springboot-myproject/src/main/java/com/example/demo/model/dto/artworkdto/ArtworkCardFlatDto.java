package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;


// 作品卡片列表組裝所用的扁平dto
public record ArtworkCardFlatDto(
		Integer artworkId,
	    String title,
	    String imageUrl,
	    LocalDateTime uploaded,
	    Integer authorId,
	    String authorName,
	    Long likes
	    ) {}
