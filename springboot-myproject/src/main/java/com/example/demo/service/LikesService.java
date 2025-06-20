package com.example.demo.service;

import java.util.List;
import java.util.Map;

public interface LikesService {
	void likeArtwork(Integer userId, Integer artworkId);

	void unlikeArtwork(Integer userId, Integer artworkId);

	Integer getLikeCount(Integer artworkId);

	boolean hasLiked(Integer userId, Integer artworkId);
	
	Map<Integer, Integer> getLikesCountMap(List<Integer> artworkIds);
}
