package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Artwork;

public interface ArtworkService {
	
	ArtworkDisplayDto uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto);
	
	ArtworkDisplayDto getArtworkDisplayDto(Integer artworkId);
	
	List<ArtworkDisplayDto> getAllArtworkDtos();
	
	List<ArtworkDisplayDto> getArtworkDtosByUser(Integer userId);
	
	List<ArtworkDisplayDto> getArtworkDtosByTag(String tagname);
	
	void deleteArtwork(Integer artworkId, String token);
	
}
