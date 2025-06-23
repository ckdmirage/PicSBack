package com.example.demo.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private TagMapper tagMapper;

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

		dto.setLikes(flat.likes().intValue());
		dto.setTagDtos(tags);

		return dto;
	}

	public ArtworkCardDto toDisplayDto(FavouriteFlatDto flat, List<TagDto> tags) {
		ArtworkCardFlatDto artworkFlat = flat.artwork();

		ArtworkCardDto dto = new ArtworkCardDto();
		dto.setId(artworkFlat.artworkId());
		dto.setTitle(artworkFlat.title());
		dto.setImageUrl(artworkFlat.imageUrl());
		dto.setUploaded(artworkFlat.uploaded());

		// ✅ 改為使用 UserBriefDto
		UserBriefDto authorDto = new UserBriefDto(artworkFlat.authorId(), artworkFlat.authorName());
		dto.setAuthor(authorDto);

		dto.setLikes(artworkFlat.likes() != null ? artworkFlat.likes().intValue() : 0);
		dto.setTagDtos(tags);

		return dto;
	}



	public ArtworkCardDto toDisplayDto(ArtworkCardFlatDto flat, List<TagDto> tags, int likes,
			boolean likedByCurrentUser) {
		// ✅ 改為 UserBriefDto
		UserBriefDto author = new UserBriefDto(flat.authorId(), flat.authorName());

		return new ArtworkCardDto(flat.artworkId(), flat.title(), flat.imageUrl(), flat.uploaded(), author, tags,
				likes, likedByCurrentUser);
	}
	
	public ArtworkDetailDto toDetailDto(ArtworkDetailFlatDto flat, List<TagDto> tags, boolean liked) {
	    ArtworkDetailDto dto = new ArtworkDetailDto();
	    dto.setId(flat.artworkId());
	    dto.setTitle(flat.title());
	    dto.setImageUrl(flat.imageUrl());
	    dto.setUploaded(flat.uploaded());

	    // ✅ 這裡用攤平欄位，避免 N+1 問題
	    dto.setAuthorId(flat.authorId());
	    dto.setAuthorName(flat.authorName());
	    dto.setAuthorEmail(flat.authorEmail());
	    dto.setAuthorAvatarUrl(flat.authorAvatarUrl());
	    dto.setAuthorRegisted(flat.authorRegisted());

	    dto.setTagDtos(tags);
	    dto.setLikes(flat.likes() != null ? flat.likes().intValue() : 0);
	    dto.setLikedByCurrentUser(liked);

	    return dto;
	}


}