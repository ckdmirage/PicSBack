package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ArtworkException;
import com.example.demo.exception.FavouriteException;
import com.example.demo.exception.UserNoFoundException;
import com.example.demo.mapper.ArtworkMapper;
import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.Favourite;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.serializable.FavouriteId;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.FavouriteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FavouriteService;

import jakarta.transaction.Transactional;

@Service
public class FavouriteServiceImpl implements FavouriteService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArtworkRepository artworkRepository;

	@Autowired
	private FavouriteRepository favouriteRepository;

	@Autowired
	private ArtworkMapper artworkMapper;

	@Override
	@Transactional
	public void addFavourite(Integer userId, Integer artworkId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNoFoundException("找不到此用戶"));
		Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new ArtworkException("找不到此作品"));
		FavouriteId favouriteId = new FavouriteId(userId, artworkId);
		if (favouriteRepository.existsById(favouriteId)) {
			throw new FavouriteException("已收藏過該作品");
		}
		Favourite favourite = new Favourite(favouriteId, user, artwork, null);
		favouriteRepository.save(favourite);
	}

	@Override
	@Transactional
	public void removeFavourite(Integer userId, Integer artworkId) {
		FavouriteId favouriteId = new FavouriteId(userId, artworkId);
		if (!favouriteRepository.existsById(favouriteId)) {
			throw new FavouriteException("你還沒有收藏過該作品");
		}
		favouriteRepository.deleteById(favouriteId);
	}

	@Override
	public boolean hasFavourited(Integer userId, Integer artworkId) {
		FavouriteId favouriteId = new FavouriteId(userId, artworkId);
		return favouriteRepository.existsById(favouriteId);
	}

	@Override
	public List<ArtworkDisplayDto> getMyFavourites(Integer userId) {
		List<Favourite> favourites = favouriteRepository.findByUserIdOrderByCreateAtDesc(userId);
		List<Artwork> artworks = favourites.stream().map(Favourite::getArtwork).toList();
		return artworks.stream().map(artwork -> artworkMapper.toDisplayDto(artwork)).toList();
	}

}
