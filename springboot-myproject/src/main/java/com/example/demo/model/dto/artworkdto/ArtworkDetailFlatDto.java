package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;

// 前端作品頁扁平dto
public record ArtworkDetailFlatDto(
	    Integer artworkId,
	    String title,
	    String imageUrl,
	    LocalDateTime uploaded,
	    
	    Integer authorId,
	    String authorName,
	    String authorEmail,
	    String authorAvatarUrl, 
	    LocalDateTime authorCreated,
	    Long likes 
	) {}