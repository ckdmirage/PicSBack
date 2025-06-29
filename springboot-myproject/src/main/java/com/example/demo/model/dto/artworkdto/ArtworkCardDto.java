package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.userdto.UserBriefDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//前端作品卡片渲染用dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkCardDto {
	private Integer id;
	private String title;
	private String imageUrl;
	private LocalDateTime uploaded;
	
	private UserBriefDto author;
	private List<TagDto> tagDtos;
	private Long likes;
	
	private boolean likedByCurrentUser;
}
