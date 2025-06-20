package com.example.demo.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.dto.artworkdto.ArtworkDisplayFlatDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.entity.Artwork;

@Component
public class ArtworkMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    // 詳情頁用：Artwork → ArtworkDisplayDto
    public ArtworkDisplayDto toDisplayDto(Artwork artwork) {
        ArtworkDisplayDto dto = modelMapper.map(artwork, ArtworkDisplayDto.class);
        dto.setAuthor(userMapper.toDto(artwork.getUser()));
        dto.setTagDtos(artwork.getTags().stream().map(tagMapper::toDto).toList());
        return dto;
    }

    // 列表頁用：FlatDto + tags → DisplayDto（手動補 likes 與 author）
    public ArtworkDisplayDto toDisplayDto(ArtworkDisplayFlatDto flat, List<TagDto> tags) {
        UserDto authorDto = new UserDto(flat.authorId(), flat.authorName(), null, null, null);
        return new ArtworkDisplayDto(
        	    flat.artworkId(),
        	    flat.title(),
        	    flat.imageUrl(),
        	    flat.uploaded(),
        	    authorDto,
        	    tags,
        	    Integer.valueOf(flat.likes().intValue())
        );
    }
}