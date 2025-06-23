package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.userdto.UserBriefDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkDetailDto {
	private Integer id;
	private String title;
	private String imageUrl;
	private LocalDateTime uploaded;

	private Integer authorId;
	private String authorName;
	private String authorAvatarUrl;
	private String authorEmail;
	private LocalDateTime authorRegisted;
	
	private List<TagDto> tagDtos;
	private Integer likes;

	private boolean likedByCurrentUser;
}
