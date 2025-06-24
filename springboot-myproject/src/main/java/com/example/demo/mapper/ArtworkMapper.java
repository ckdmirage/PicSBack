package com.example.demo.mapper;

import java.util.List;


import org.springframework.stereotype.Component;

import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.artworkdto.ArtworkCardDto;
import com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto;
import com.example.demo.model.dto.artworkdto.ArtworkDetailDto;
import com.example.demo.model.dto.artworkdto.ArtworkDetailFlatDto;
import com.example.demo.model.dto.favouriteDto.FavouriteFlatDto;
import com.example.demo.model.dto.userdto.UserBriefDto;
import com.example.demo.model.dto.userdto.UserDto;

@Component
public class ArtworkMapper {

	// 列表頁用：FlatDto + tags → DisplayDto（手動補 likes 與 author）
	public ArtworkCardDto toDisplayDto(ArtworkCardFlatDto flat, List<TagDto> tags) {
		ArtworkCardDto dto = new ArtworkCardDto();
		dto.setId(flat.artworkId());
		dto.setTitle(flat.title());
		dto.setImageUrl(flat.imageUrl());
		dto.setUploaded(flat.uploaded());

		// ✅ 使用 UserBriefDto（只包含 id 與 username）
		UserBriefDto authorDto = new UserBriefDto(flat.authorId(), flat.authorName());
		dto.setAuthor(authorDto);

		dto.setLikes(flat.likes());
		dto.setTagDtos(tags);

		return dto;
	}

	public ArtworkCardDto toCardDto(FavouriteFlatDto flat, List<TagDto> tags) {
	    ArtworkCardDto dto = new ArtworkCardDto();
	    dto.setId(flat.artworkId());
	    dto.setTitle(flat.title());
	    dto.setImageUrl(flat.imageUrl());
	    dto.setUploaded(flat.uploaded());

	    UserBriefDto authorDto = new UserBriefDto(flat.authorId(), flat.authorName());
	    dto.setAuthor(authorDto);

	    dto.setLikes(flat.likes() != null ? flat.likes() : 0);
	    dto.setTagDtos(tags);

	    return dto;
	}




	public ArtworkCardDto toCardDto(ArtworkCardFlatDto flat, List<TagDto> tags, long likes,
			boolean likedByCurrentUser) {
		// ✅ 改為 UserBriefDto
		UserBriefDto author = new UserBriefDto(flat.authorId(), flat.authorName());

		return new ArtworkCardDto(flat.artworkId(), flat.title(), flat.imageUrl(), flat.uploaded(), author, tags,
				likes, likedByCurrentUser);
	}
	
	public ArtworkDetailDto toDetailDto(ArtworkDetailFlatDto flat, List<TagDto> tags, boolean liked) {
	    // ✅ 建立作者 UserDto（不會 N+1，因為都是 flat 來的）
	    UserDto author = new UserDto(
	        flat.authorId(),
	        flat.authorName(),
	        flat.authorEmail(),
	        flat.authorCreated(),    // 注意欄位名稱一致
	        flat.authorAvatarUrl()
	    );

	    ArtworkDetailDto dto = new ArtworkDetailDto();
	    dto.setId(flat.artworkId());
	    dto.setTitle(flat.title());
	    dto.setImageUrl(flat.imageUrl());
	    dto.setUploaded(flat.uploaded());

	    dto.setAuthor(author); // ✅ 用 UserDto 替代原本 authorId、authorName...

	    dto.setTagDtos(tags);
	    dto.setLikes(flat.likes() != null ? flat.likes() : 0);
	    dto.setLikedByCurrentUser(liked);

	    return dto;
	}



}