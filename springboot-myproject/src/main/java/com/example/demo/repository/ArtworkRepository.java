package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	// 首頁（全部作品）可排序、可分頁
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded,
			        u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			""")
	Page<ArtworkCardFlatDto> findAllWithLikes(Pageable pageable);
	
	// 首頁 根據讚數排序（單獨查詢）
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
		Page<ArtworkCardFlatDto> findAllOrderByLikes(Pageable pageable);

	// 作者頁：依 userId 查詢，可排序、可分頁
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
			""")
	Page<ArtworkCardFlatDto> findByUserIdWithLikes(@Param("userId") Integer userId, Pageable pageable);
	
	// 作者頁 根據讚數排序（單獨查詢）
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
		Page<ArtworkCardFlatDto> findByUserIdOrderByLikes(@Param("userId") Integer userId, Pageable pageable);

	// 標籤頁 依 tagName 查詢，可排序、可分頁
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
			""")
	Page<ArtworkCardFlatDto> findByTagNameWithLikes(@Param("tagName") String tagName, Pageable pageable);

	// 標簽頁 根據讚數排序（單獨查詢）
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
	Page<ArtworkCardFlatDto> findByTagNameOrderByLikes(@Param("tagName") String tagName, Pageable pageable);

	// 模糊搜尋：依 title 查詢，可排序、可分頁
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
			""")
	Page<ArtworkCardFlatDto> searchByTitleWithLikes(@Param("keyword") String keyword, Pageable pageable);

	// 模糊搜尋：根據讚數排序（單獨查詢）
	@Query("""
			    SELECT new com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto(
			        a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username, COUNT(l)
			    )
			    FROM Artwork a
			    JOIN a.user u
			    LEFT JOIN Likes l ON l.artwork.id = a.id
			    WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    GROUP BY a.id, a.title, a.imageUrl, a.uploaded, u.id, u.username
			    ORDER BY COUNT(l) DESC
			""")
	Page<ArtworkCardFlatDto> searchByTitleOrderByLikes(@Param("keyword") String keyword, Pageable pageable);

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
