package com.example.demo.service;

public interface LikesService {
	void likeArtwork(Integer userId, Integer artworkId);

	void unlikeArtwork(Integer userId, Integer artworkId);

	int getLikeCount(Integer artworkId);

	boolean hasLiked(Integer userId, Integer artworkId);
}
