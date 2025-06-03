package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ArtworkDisplayDto {
	private Integer id;
	private String title;
	private String imageUrl;
	private LocalDateTime uploaded;
	private String authorUsername;
	private List<String> tagNames;
}
