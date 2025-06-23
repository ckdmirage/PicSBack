package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.example.demo.model.dto.favouriteDto.FavouriteFlatDto;
import com.example.demo.model.entity.Favourite;
import com.example.demo.model.entity.serializable.FavouriteId;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId>{
	
	boolean existsById (FavouriteId id);
	
	void deleteById(FavouriteId id);
	
	@EntityGraph(attributePaths = {"artwork"})
	List<Favourite> findByUserIdOrderByCreateAtDesc(Integer userId);
	
	@Query("""
		    select new com.example.demo.model.dto.favouriteDto.FavouriteFlatDto(
		        new com.example.demo.model.dto.artworkdto.ArtworkDisplayFlatDto(
		            a.id, a.title, a.imageUrl, a.uploaded,
		            u.id, u.username,
		            count(l)
		        ),
		        f.createAt
		    )
		    from Favourite f
		    join f.artwork a
		    join a.user u
		    left join Likes l on l.artwork.id = a.id
		    where f.user.id = :userId
		    group by a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username, f.createAt
		    order by f.createAt desc
		""")
		List<FavouriteFlatDto> findMyFavouriteFlatDtos(Integer userId);
	
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

}
