package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Likes;
import com.example.demo.model.entity.serializable.LikeId;

@Repository
public interface LikesRepository extends JpaRepository<Likes, LikeId>{
	
	//計算點讚
	//int countByArtworkId(Integer artworkId);
	
	//該用戶是否點讚
    boolean existsByArtworkIdAndUserId(Integer artworkId, Integer userId);
    //取消點讚
    void deleteByArtworkIdAndUserId(Integer artworkId, Integer userId);
    
    @Query("SELECT l.artworkId, COUNT(l) FROM Likes l WHERE l.artworkId IN :ids GROUP BY l.artworkId")
    List<Object[]> countLikesByArtworkIds(@Param("ids") List<Integer> ids);
    
    @Query("""
    	    select l.artwork.id
    	    from Likes l
    	    where l.userId = :userId and l.artwork.id in :artworkIds
    	""")
    	List<Integer> findLikedArtworkIds(@Param("userId") Integer userId, @Param("artworkIds") List<Integer> artworkIds);

    //供刪除作品時相關點讚記錄
    void deleteByArtworkId(Integer artworkId);
}
