package com.example.demo.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.Tag;
import com.example.demo.repository.LikesRepository;

@Component
public class ArtworkMapper {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private LikesRepository likesRepository;
	
	
	public ArtworkDisplayDto toDisplayDto(Artwork artwork) {
		ArtworkDisplayDto artworkDisplayDto = modelMapper.map(artwork,ArtworkDisplayDto.class);
		artworkDisplayDto.setAuthorId(artwork.getUser().getId());
		artworkDisplayDto.setAuthorname(artwork.getUser().getUsername());
		artworkDisplayDto.setTagNames(artwork.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
		return artworkDisplayDto;
	}
}
