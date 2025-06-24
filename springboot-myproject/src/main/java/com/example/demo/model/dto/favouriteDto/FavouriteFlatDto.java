package com.example.demo.model.dto.favouriteDto;

import java.time.LocalDateTime;

public record FavouriteFlatDto(
	    Integer artworkId,
	    String title,
	    String imageUrl,
	    LocalDateTime uploaded,
	    Integer authorId,
	    String authorName,
	    Long likes,
	    LocalDateTime favouriteTime
	) {}
