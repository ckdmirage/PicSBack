package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Favourite;
import com.example.demo.model.entity.serializable.FavouriteId;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId>{
	
	boolean existsById (FavouriteId id);
	
	void deleteById(FavouriteId id);
	
	@EntityGraph(attributePaths = {"artwork"})
	List<Favourite> findByUserIdOrderByCreateAtDesc(Integer userId);
}
