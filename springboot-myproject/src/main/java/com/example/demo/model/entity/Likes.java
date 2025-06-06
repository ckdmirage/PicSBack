package com.example.demo.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.demo.model.entity.serializable.LikeId;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@IdClass(LikeId.class)
@NoArgsConstructor
@AllArgsConstructor	
public class Likes {
    @Id
    private Integer userId;

    @Id
    private Integer artworkId;

    private LocalDateTime likeCreatedAt;


}


