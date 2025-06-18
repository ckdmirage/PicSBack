package com.example.demo.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.repository.LikesRepository;

@Component
public class ArtworkMapper {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TagMapper tagMapper;
	@Autowired
	private LikesRepository likesRepository;
	
	
	public ArtworkDisplayDto toDisplayDto(Artwork artwork) {
	    ArtworkDisplayDto dto = modelMapper.map(artwork, ArtworkDisplayDto.class);

	    dto.setAuthor(userMapper.toDto(artwork.getUser()));

	    dto.setTagDtos(
	        artwork.getTags().stream()
	            .map(tagMapper::toDto)
	            .collect(Collectors.toList())
	    );
	    return dto;
	}
}
