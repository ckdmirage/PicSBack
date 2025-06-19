package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Artwork;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Integer>{
	@EntityGraph(attributePaths = {"tags", "user"})
    List<Artwork> findByUserId(Integer userId);
	
	//獲取單作品及其tag
	@Query("SELECT a FROM Artwork a LEFT JOIN FETCH a.tags LEFT JOIN FETCH a.user WHERE a.id = :artworkId")
	Optional<Artwork> findByIdWithTags(@Param("artworkId") Integer artworkId);
	
	@EntityGraph(attributePaths = {"tags", "user"})
	@Query("SELECT a FROM Artwork a JOIN a.tags t WHERE t.name = :tagName")
	List<Artwork> findByTagName(@Param("tagName") String tagName);
	
	@EntityGraph(attributePaths = {"tags", "user"})
	@Query("SELECT a FROM Artwork a") // 可以省略，保留方便看
	List<Artwork> findAllWithTagsUser();
	
	@EntityGraph(attributePaths = {"user", "tags"})
	@Query("SELECT a FROM Artwork a WHERE a.id = :id")
	Optional<Artwork> findWithUserAndTagsById(@Param("id") Integer id);

	
	//模糊搜索
	List<Artwork> findByTitleContainingIgnoreCaseOrderByUploadedDesc(String keyword);
	List<Artwork> findByTitleContainingIgnoreCaseOrderByUploadedAsc(String keyword);
	
	@Query(value = """
		    SELECT a.* FROM artwork a
		    LEFT JOIN likes l ON a.id = l.artwork_id
		    WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    GROUP BY a.id
		    ORDER BY COUNT(l.artwork_id) DESC
		""", nativeQuery = true)
		List<Artwork> findMostLikedByTitle(@Param("keyword") String keyword);

}
	