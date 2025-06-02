package com.example.demo.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artwork")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artwork {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "artwork_id")
	private Integer id;

	// 關聯上傳用戶
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(name = "image_url", length = 255, nullable = false, unique = true)
	private String imageUrl;

	@Column(name = "artwork_created_at")
	private LocalDateTime artworkCreatedAt = LocalDateTime.now();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "artwork_tag", joinColumns = @JoinColumn(name = "artwork_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags = new ArrayList<>();

}
/*
	 * CREATE TABLE artwork( artwork_id INT AUTO_INCREMENT PRIMARY KEY COMMENT
	 * "作品ID", user_id INT NOT NULL COMMENT "作品所屬用戶ID", title VARCHAR(100) NOT NULL
	 * COMMENT "標題", image_url VARCHAR(255) NOT NULL COMMENT "作品存儲位置",
	 * artwork_created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "作品上傳時間",
	 * FOREIGN KEY (user_id) REFERENCES user(user_id) );
	 */