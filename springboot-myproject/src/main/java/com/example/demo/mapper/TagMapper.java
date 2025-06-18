package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.TagDto;
import com.example.demo.model.entity.Tag;

@Component
public class TagMapper {
	@Autowired
	private ModelMapper modelMapper;
	
	public TagDto toDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    public Tag toEntity(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }
}
