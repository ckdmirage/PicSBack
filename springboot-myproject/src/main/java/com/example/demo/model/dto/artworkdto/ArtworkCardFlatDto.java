package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;


// 作品列表組裝dto時所用查詢所有信息的扁平dto
public record ArtworkCardFlatDto(
		Integer artworkId,
	    String title,
	    String imageUrl,
	    LocalDateTime uploaded,
	    Integer authorId,
	    String authorName,
	    Long likes
	    ) {}
