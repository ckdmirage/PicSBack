package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto;
import com.example.demo.model.dto.artworkdto.ArtworkDetailFlatDto;
import com.example.demo.model.dto.artworkdto.ArtworkTagDto;
import com.example.demo.model.entity.Artwork;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Integer> {

	// 查詢單筆作品
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkDetailFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, u.email, u.avatarUrl, u.created,
			        COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE a.id = :artworkId
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded,
			             u.id, u.username, u.email, u.avatarUrl, u.created
			""")
	Optional<ArtworkDetailFlatDto> findDetailFlatById(@Param("artworkId") Integer artworkId);

	// 查詢所有作品：從新到舊
	@Query("""
					SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
					a.id, a.title, a.imageUrl, a.uploaded,
					u.id, u.username, COUNT(l)
					)
					FROM Artwork a
					JOIN a.user u
					LEFT JOIN Likes l ON l.artwork.id = a.id
					GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
					ORDER BY a.uploaded DESC
			""")
	List<ArtworkCardFlatDto> findAllOrderByNewest();

	// 查詢所有作品：從舊到新
	@Query("""
					SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
					a.id, a.title, a.imageUrl, a.uploaded,
					u.id, u.username, COUNT(l)
					)
					FROM Artwork a
					JOIN a.user u
					LEFT JOIN Likes l ON l.artwork.id = a.id
					GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
					ORDER BY a.uploaded ASC
			""")
	List<ArtworkCardFlatDto> findAllOrderByOldest();

	// 查詢所有作品：按點讚
	@Query("""
					SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
					a.id, a.title, a.imageUrl, a.uploaded,
					u.id, u.username, COUNT(l)
					)
					FROM Artwork a
					JOIN a.user u
					LEFT JOIN Likes l ON l.artwork.id = a.id
					GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
					ORDER BY COUNT(l) DESC
			""")
	List<ArtworkCardFlatDto> findAllOrderByMostLiked();

	// 根據作者：從新到舊
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE u.id = :userId
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY a.uploaded DESC
			""")
	List<ArtworkCardFlatDto> findByUserIdOrderByNewest(@Param("userId") Integer userId);

	// 根據作者：按舊到新
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE u.id = :userId
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY a.uploaded ASC
			""")
	List<ArtworkCardFlatDto> findByUserIdOrderByOldest(@Param("userId") Integer userId);

	// 根據作者：按讚數
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE u.id = :userId
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY COUNT(l) DESC
			""")
	List<ArtworkCardFlatDto> findByUserIdOrderByMostLiked(@Param("userId") Integer userId);

	// 根據標籤：從新到舊
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    JOIN a.tags t
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE t.name = :tagName
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY a.uploaded DESC
			""")
	List<ArtworkCardFlatDto> findByTagNameOrderByNewest(@Param("tagName") String tagName);

	// 根據標籤：從舊到新
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    JOIN a.tags t
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE t.name = :tagName
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY a.uploaded ASC
			""")
	List<ArtworkCardFlatDto> findByTagNameOrderByOldest(@Param("tagName") String tagName);

	// 根據標籤：按讚數
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    JOIN a.tags t
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE t.name = :tagName
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY COUNT(l) DESC
			""")
	List<ArtworkCardFlatDto> findByTagNameOrderByMostLiked(@Param("tagName") String tagName);

	// 模糊搜尋：從新到舊
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY a.uploaded DESC
			""")
	List<ArtworkCardFlatDto> searchByTitleNewest(@Param("keyword") String keyword);

	// 模糊搜尋：從舊到新
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY a.uploaded ASC
			""")
	List<ArtworkCardFlatDto> searchByTitleOldest(@Param("keyword") String keyword);

	// 模糊搜尋：按點讚
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY COUNT(l) DESC
			""")
	List<ArtworkCardFlatDto> searchByTitleMostLiked(@Param("keyword") String keyword);

	// 一次查所有作品對應的 tag 資訊
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkTagDto(a.id, t.id, t.name)
			    FROM Artwork a
			    JOIN a.tags t
			    WHERE a.id IN :artworkIds
			""")
	List<ArtworkTagDto> findTagTuplesByArtworkIds(@Param("artworkIds") List<Integer> artworkIds);

	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE a.id IN :artworkIds
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			""")
	List<ArtworkCardFlatDto> findCardFlatDtoByIds(@Param("artworkIds") List<Integer> artworkIds);

}
