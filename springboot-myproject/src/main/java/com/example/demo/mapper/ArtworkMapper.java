package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.entity.Artwork;

@Component
public class ArtworkMapper {
	@Autowired
	private ModelMapper modelMapper;
	
	public Artwork toEntity(ArtworkDisplayDto artworkDisplayDto) {
		return modelMapper.map(artworkDisplayDto, Artwork.class);
	}
	
	public ArtworkDisplayDto toDisplayDto(Artwork artwork) {
		return modelMapper.map(artwork,ArtworkDisplayDto.class);
	}
}
