package com.example.demo.model.entity;

import java.time.LocalDateTime;

import com.example.demo.model.entity.serializable.LikeId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "artwork_id")
    private Integer artworkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id", insertable = false, updatable = false)
    private Artwork artwork;

    private LocalDateTime likeCreatedAt;
}

