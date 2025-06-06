package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.entity.Likes;
import com.example.demo.model.entity.serializable.LikeId;

@Repository
public interface LikesRepository extends JpaRepository<Likes, LikeId>{
	//計算點讚
	int countByArtworkId(Integer artworkId);
	//該用戶是否點讚
    boolean existsByArtworkIdAndUserId(Integer artworkId, Integer userId);
    //取消點讚
    void deleteByArtworkIdAndUserId(Integer artworkId, Integer userId);
}
