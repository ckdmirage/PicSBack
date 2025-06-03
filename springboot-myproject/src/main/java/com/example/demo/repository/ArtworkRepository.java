package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Artwork;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Integer>{
	@Query("SELECT a FROM Artwork a WHERE a.user.id = :userId")
	List<Artwork> getArtworks(@Param("userId") Integer userId);
}
