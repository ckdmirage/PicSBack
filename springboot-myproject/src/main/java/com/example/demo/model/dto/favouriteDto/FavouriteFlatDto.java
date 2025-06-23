package com.example.demo.model.dto.favouriteDto;

import java.time.LocalDateTime;

import com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto;

public record FavouriteFlatDto(
		ArtworkCardFlatDto artwork,
	    LocalDateTime favouriteTime

	) {}
