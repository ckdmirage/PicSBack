package com.example.demo.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//檢舉功能
@Entity
@Data
@Table(name = "report")
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id @GeneratedValue
    private Integer id;

    @ManyToOne
    private User reporter;

    @ManyToOne
    private Artwork artwork;

    private String reason;
    private LocalDateTime reportedAt;
}