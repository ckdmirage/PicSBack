package com.example.demo.model.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "follow")
public class Follow {
	@EmbeddedId
	private Integer in;
}
