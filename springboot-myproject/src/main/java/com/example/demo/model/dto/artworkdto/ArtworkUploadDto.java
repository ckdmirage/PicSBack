package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtworkUploadDto {
	@NotBlank(message = "作品名不能為空")
	private String title;
	@NotBlank(message = "未上傳作品")
	private String imageUrl;
	private LocalDateTime uploaded = LocalDateTime.now();
	private List<Integer> tagIds; 
}
