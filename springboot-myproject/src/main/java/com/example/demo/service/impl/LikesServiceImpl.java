package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.LikesException;
import com.example.demo.model.entity.Likes;
import com.example.demo.model.entity.User;
import com.example.demo.repository.LikesRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LikesService;

@Service
public class LikesServiceImpl implements LikesService {

	@Autowired
	private LikesRepository likesRepository;
	@Autowired 
	private UserRepository userRepository;

	@Override
	public void likeArtwork(Integer userId, Integer artworkId) {
		
		String username = userRepository.findById(userId).get().getUsername();

		if (likesRepository.existsByArtworkIdAndUserId(artworkId, userId)) {
			throw new LikesException("該用戶已經點過讚");
		}
		Likes like = new Likes(userId, artworkId, LocalDateTime.now());
		likesRepository.save(like);

	}

	@Override
	public void unlikeArtwork(Integer userId, Integer artworkId) {
		
		if (!likesRepository.existsByArtworkIdAndUserId(artworkId, userId)) {
			throw new LikesException("該用戶並未點讚");
		}
		likesRepository.deleteByArtworkIdAndUserId(artworkId, userId);
	}

	@Override
	public int getLikeCount(Integer artworkId) {
		return likesRepository.countByArtworkId(artworkId);
	}

	//判斷是否點讚
	@Override
	public boolean hasLiked(Integer userId, Integer artworkId) {
		return likesRepository.existsByArtworkIdAndUserId(artworkId, userId); // 點了回應true, 沒有回應false
	}

}
