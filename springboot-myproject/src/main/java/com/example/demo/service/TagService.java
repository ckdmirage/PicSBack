package com.example.demo.service;

import java.util.List;

import com.example.demo.exception.TagException;
import com.example.demo.model.dto.tagdto.TagCreateDto;
import com.example.demo.model.entity.Tag;

public interface TagService {
	
	Tag addTag(TagCreateDto tagDto);
	
	List<Tag> searchTags(String keyword);
}
