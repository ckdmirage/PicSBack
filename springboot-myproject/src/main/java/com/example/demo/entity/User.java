package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer id;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String email;
	
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	
	@Column(name = "email_verified", nullable = false)
	private Boolean emailVerified = false; //默認為未通過
	
	@Column(name = "user_created_at")
	private LocalDateTime userCreatedAt = LocalDateTime.now();
	
}

/*
   CREATE TABLE user(
			user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT "用戶ID",
    		username VARCHAR(50) NOT NULL UNIQUE COMMENT "用戶名",
    		email VARCHAR(100) NOT NULL COMMENT "郵箱",
    		password_hash VARCHAR(255) NOT NULL  COMMENT "密碼",
			email_verified BOOLEAN DEFAULT FALSE COMMENT "郵箱驗證",
    	user_created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "創建時間"
	);
 */
/*
 * USE myproject;
CREATE TABLE user(
	user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT "用戶ID",
    username VARCHAR(50) NOT NULL UNIQUE COMMENT "用戶名",
    email VARCHAR(100) NOT NULL COMMENT "郵箱",
    password_hash VARCHAR(255) NOT NULL  COMMENT "密碼",
	email_verified BOOLEAN DEFAULT FALSE COMMENT "郵箱驗證",
    user_created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "創建時間"
);
ALTER TABLE artwork CHANGE artwork_create_at artwork_created_at INT;

CREATE TABLE  artwork(
	artwork_id INT AUTO_INCREMENT PRIMARY KEY COMMENT "作品ID",
    user_id INT NOT NULL COMMENT "作品所屬用戶ID",
    title VARCHAR(100) NOT NULL COMMENT "標題",
    image_url VARCHAR(255) NOT NULL COMMENT "作品存儲位置",
    artwork_created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "作品上傳時間",
	FOREIGN KEY (user_id) REFERENCES user(user_id)
);
CREATE TABLE tag(
	tag_id INT AUTO_INCREMENT PRIMARY KEY COMMENT "標籤id",
    tag_name VARCHAR(50) NOT NULL UNIQUE COMMENT "標籤名稱"
);

CREATE TABLE artwork_tag(
	artwork_id INT NOT NULL COMMENT "作品id",
    tag_id INT NOT NULL COMMENT "標籤id",
    PRIMARY KEY (artwork_id, tag_id),
    FOREIGN KEY (artwork_id) REFERENCES artwork(artwork_id),
    FOREIGN KEY (tag_id) REFERENCES tag(tag_id)
);

CREATE TABLE favorite(
	user_id INT NOT NULL COMMENT "收藏者的用戶ID",
    artwork_id INT NOT NULL COMMENT "收藏的作品ID",
    favorite_created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "收藏時間",
	PRIMARY KEY (user_id, artwork_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (artwork_id) REFERENCES artwork(artwork_id)
);

CREATE TABLE fallow(
	follower_id INT NOT NULL COMMENT "追蹤者ID",
    followee_id INT NOT NULL COMMENT "被追蹤者ID",
    fallow_created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "訂閱時間",
    PRIMARY KEY (follower_id, followee_id),
	FOREIGN KEY (follower_id) REFERENCES user(user_id),
    FOREIGN KEY (followee_id) REFERENCES user(user_id)
);

CREATE TABLE likes(
	user_id INT NOT NULL COMMENT "點讚者id",
    artwork_id INT NOT NULL COMMENT "點讚作品ID",
    like_created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "點讚時間",
    PRIMARY KEY(user_id, artwork_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (artwork_id) REFERENCES artwork(artwork_id)
);


CREATE TABLE comment(
	comment_id INT AUTO_INCREMENT PRIMARY KEY COMMENT "留言ID",
    artwork_id INT NOT NULL COMMENT "所留言的作品的ID",
    user_id INT NOT NULL COMMENT "留言者用戶ID",
    comment TEXT NOT NULL COMMENT "留言內容",
    comment_create_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "留言時間",
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (artwork_id) REFERENCES artwork(artwork_id)
);

 * */
