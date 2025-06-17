package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.model.dto.TagDto;

import lombok.Data;

@Data
public class ArtworkDisplayDto {
	private Integer id;
	private String title;
	private String imageUrl;
	private LocalDateTime uploaded = LocalDateTime.now();;
	
	private Integer authorId;
	private String authorname;
	private List<TagDto> tagDtos;
	private Integer likes;
}
