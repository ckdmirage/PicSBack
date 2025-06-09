package com.example.demo.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.Tag;

@Component
public class ArtworkMapper {
	@Autowired
	private ModelMapper modelMapper;
	
	
	
	public ArtworkDisplayDto toDisplayDto(Artwork artwork) {
		ArtworkDisplayDto artworkDisplayDto = modelMapper.map(artwork,ArtworkDisplayDto.class);
		artworkDisplayDto.setAuthorId(artwork.getUser().getId());
		artworkDisplayDto.setTagNames(artwork.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
		artworkDisplayDto.setUploaded(artwork.getArtworkCreatedAt());
		return artworkDisplayDto;
	}
}
