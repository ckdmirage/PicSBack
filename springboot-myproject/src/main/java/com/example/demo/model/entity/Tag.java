package com.example.demo.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="tag_name", nullable = false, unique = true)
	private String name;
	
	@ManyToMany(mappedBy = "tags")
	private List<Artwork> artworks = new ArrayList<>();
}
