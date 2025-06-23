package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.artworkdto.ArtworkCardDto;

public interface FavouriteService {
	void addFavourite(Integer userId, Integer artworkId);
	void removeFavourite(Integer userId, Integer artworkId);
	boolean hasFavourited(Integer userId, Integer artworkId);
	List<ArtworkCardDto> getMyFavourites(Integer userId, String sort);
}
