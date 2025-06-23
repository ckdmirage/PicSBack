package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ArtworkException;
import com.example.demo.exception.FavouriteException;
import com.example.demo.exception.UserNoFoundException;
import com.example.demo.mapper.ArtworkMapper;
import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.artworkdto.ArtworkCardDto;
import com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto;
import com.example.demo.model.dto.artworkdto.ArtworkTagDto;
import com.example.demo.model.dto.favouriteDto.FavouriteFlatDto;
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
	// 判斷是否收藏
	@Override
	public boolean hasFavourited(Integer userId, Integer artworkId) {
		FavouriteId favouriteId = new FavouriteId(userId, artworkId);
		return favouriteRepository.existsById(favouriteId);
	}
	
	// 獲得收藏作品集
	public List<ArtworkCardDto> getMyFavourites(Integer userId, String sort) {
	    // 1. 查出排序後的 artworkId
	    List<Integer> sortedArtworkIds = switch (sort) {
	        case "oldest" -> favouriteRepository.findArtworkIdsByUserOrderByOldest(userId);
	        case "liked" -> favouriteRepository.findArtworkIdsByUserOrderByMostLiked(userId);
	        default -> favouriteRepository.findArtworkIdsByUserOrderByNewest(userId);
	    };

	    if (sortedArtworkIds.isEmpty()) return List.of();

	    // 2. 查詢扁平 DTO
	    List<ArtworkCardFlatDto> flatDtos = artworkRepository.findCardFlatDtoByIds(sortedArtworkIds);

	    // 3. 查 tag
	    List<ArtworkTagDto> tagTuples = artworkRepository.findTagTuplesByArtworkIds(sortedArtworkIds);
	    Map<Integer, List<TagDto>> tagMap = new HashMap<>();
	    for (ArtworkTagDto tuple : tagTuples) {
	        tagMap.computeIfAbsent(tuple.artworkId(), k -> new ArrayList<>())
	              .add(new TagDto(tuple.tagId(), tuple.tagName()));
	    }

	    // 4. 扁平轉 DTO
	    Map<Integer, ArtworkCardDto> dtoMap = flatDtos.stream()
	        .map(flat -> artworkMapper.toDisplayDto(flat, tagMap.getOrDefault(flat.artworkId(), List.of())))
	        .collect(Collectors.toMap(ArtworkCardDto::getId, Function.identity()));

	    // 5. 排序還原
	    List<ArtworkCardDto> sortedResult = sortedArtworkIds.stream()
	        .map(dtoMap::get)
	        .filter(Objects::nonNull)
	        .toList();

	    return sortedResult;
	}



}
