package com.example.demo.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.demo.model.entity.serializable.FavouriteId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favourite")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favourite {
	@EmbeddedId
	private FavouriteId favouriteId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("artworkId")
	private Artwork artwork;
	
	@CreationTimestamp
	@Column(name = "favorite_create_at", updatable = false)
    private LocalDateTime createAt;
}
