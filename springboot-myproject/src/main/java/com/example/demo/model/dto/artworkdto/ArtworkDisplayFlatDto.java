package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;



public record ArtworkDisplayFlatDto(
		Integer artworkId,
	    String title,
	    String imageUrl,
	    LocalDateTime uploaded,
	    Integer authorId,
	    String authorName,
	    Long likes) {}
