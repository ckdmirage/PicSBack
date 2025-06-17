package com.example.demo.service;

import java.util.List;

import com.example.demo.exception.TagException;
import com.example.demo.model.dto.TagDto;
import com.example.demo.model.entity.Tag;

public interface TagService {
	
	Tag addTag(TagDto tagDto);
	
	List<TagDto> searchTags(String keyword);
	
	List<TagDto> searchByName(String keyword);
}
