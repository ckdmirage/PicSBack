package com.example.demo.model.dto.artworkdto;

// 查詢作品標簽的扁平dto
public record ArtworkTagDto(
	    Integer artworkId,
	    Integer tagId,
	    String tagName
	) {}