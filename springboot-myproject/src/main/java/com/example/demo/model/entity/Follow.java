package com.example.demo.model.entity;

import com.example.demo.model.entity.serializable.FollowId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
	@EmbeddedId
	private FollowId id;
	
	@ManyToOne
	@MapsId("followerId") //對應FollowId欄位名稱
	@JoinColumn(name = "follower_id")
	private User follower;
	
	@ManyToOne
	@MapsId("followingId")
	@JoinColumn(name = "following_id")
	private User following;
	
	
	
	
	
}
