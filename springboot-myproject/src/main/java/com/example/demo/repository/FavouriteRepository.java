package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.favouriteDto.FavouriteFlatDto;
import com.example.demo.model.entity.Favourite;
import com.example.demo.model.entity.serializable.FavouriteId;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {

	boolean existsById(FavouriteId id);

	void deleteById(FavouriteId id);
	
	//供刪除作品時相關收藏記錄
	@Query("SELECT f FROM Favourite f WHERE f.favouriteId.artworkId = :artworkId")
	List<Favourite> findByArtworkId(@Param("artworkId") Integer artworkId);

	@EntityGraph(attributePaths = { "artwork" })
	List<Favourite> findByUserIdOrderByCreateAtDesc(Integer userId);

	@Query("""
			    SELECT new com.example.demo.model.dto.favouriteDto.FavouriteFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username,
			        CAST(COUNT(l) AS long),
			        f.createAt
			    )
			    FROM Favourite f
			    JOIN f.artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE f.user.id = :userId
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username, f.createAt
			    ORDER BY f.createAt DESC
			""")
	List<FavouriteFlatDto> findMyFavouriteFlatDtos(@Param("userId") Integer userId);

	// 根據收藏：新到舊
	@Query("""
			    SELECT a.id
			    FROM Favourite f
			    JOIN f.artwork a
			    WHERE f.user.id = :userId
			    ORDER BY f.createAt DESC
			""")
	List<Integer> findArtworkIdsByUserOrderByNewest(@Param("userId") Integer userId);

	// 根據收藏：舊到新
	@Query("""
			    SELECT a.id
			    FROM Favourite f
			    JOIN f.artwork a
			    WHERE f.user.id = :userId
			    ORDER BY f.createAt ASC
			""")
	List<Integer> findArtworkIdsByUserOrderByOldest(@Param("userId") Integer userId);

	// 根據收藏：按點讚
	@Query("""
			    SELECT a.id
			    FROM Favourite f
			    JOIN f.artwork a
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE f.user.id = :userId
			    GROUP BY a.id
			    ORDER BY COUNT(l) DESC
			""")
	List<Integer> findArtworkIdsByUserOrderByMostLiked(@Param("userId") Integer userId);
	
	// 批量刪除-刪除作品用
	@Modifying
	@Query("DELETE FROM Favourite f WHERE f.artwork.id = :artworkId")
	void deleteAllByArtworkId(@Param("artworkId") Integer artworkId);

}
