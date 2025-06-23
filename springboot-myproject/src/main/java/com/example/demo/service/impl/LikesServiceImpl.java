package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ArtworkException;
import com.example.demo.exception.LikesException;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.Likes;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.LikesRepository;
import com.example.demo.service.LikesService;

import jakarta.transaction.Transactional;

@Service
public class LikesServiceImpl implements LikesService {

	@Autowired
	private LikesRepository likesRepository;
	
	@Autowired
	private ArtworkRepository artworkRepository;

	//點讚
	@Override
	@Transactional
	public void likeArtwork(Integer userId, Integer artworkId) {

		if (likesRepository.existsByArtworkIdAndUserId(artworkId, userId)) {
			throw new LikesException("該用戶已經點過讚");
		}
		Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(()->new ArtworkException("找不到該作品"));
		Likes like = new Likes(userId, artworkId, artwork, LocalDateTime.now());
		likesRepository.save(like);

	}
	
	//取消點讚
	@Override
	@Transactional
	public void unlikeArtwork(Integer userId, Integer artworkId) {
		
		if (!likesRepository.existsByArtworkIdAndUserId(artworkId, userId)) {
			throw new LikesException("該用戶並未點讚");
		}
		likesRepository.deleteByArtworkIdAndUserId(artworkId, userId);
	}
	
	//作品讚數
	//@Override
	//public Integer getLikeCount(Integer artworkId) {
	//	return likesRepository.countByArtworkId(artworkId);
	//}

	
	//判斷是否點讚(前端渲染用)
	@Override
	public boolean hasLiked(Integer userId, Integer artworkId) {
		return likesRepository.existsByArtworkIdAndUserId(artworkId, userId); // 點了回應true, 沒有回應false
	}
	
	
	@Override
	public Map<Integer, Integer> getLikesCountMap(List<Integer> artworkIds) {
	    List<Object[]> results = likesRepository.countLikesByArtworkIds(artworkIds);
	    return results.stream().collect(Collectors.toMap(
	        row -> (Integer) row[0],
	        row -> ((Long) row[1]).intValue()
	    ));
	}
}
