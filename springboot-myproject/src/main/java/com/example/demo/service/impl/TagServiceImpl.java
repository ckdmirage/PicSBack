package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.exception.TagException;
import com.example.demo.model.dto.tagdto.TagCreateDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.Tag;
import com.example.demo.repository.TagRepository;
import com.example.demo.service.TagService;

public class TagServiceImpl implements TagService{
	@Autowired
	private TagRepository tagRepository;
	
	@Override
	public Tag addTag(TagCreateDto tagCreateDto) throws TagException {
		Optional<Tag> optTag = tagRepository.getTag(tagCreateDto.getName());
		if(optTag.isPresent()) {
			throw new TagException("標籤重複!");
		}
		Tag tag = new Tag(null, tagCreateDto.getName(), new ArrayList<Artwork>());
		return tagRepository.save(tag);
	}

	@Override
	public List<Tag> searchTags(String keyword) {
		return tagRepository.findByNameContainingIgnoreCase(keyword);
	}

}
