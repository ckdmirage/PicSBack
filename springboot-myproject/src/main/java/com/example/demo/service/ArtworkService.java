package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Artwork;

public interface ArtworkService {
	
	ArtworkDisplayDto uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto);
	
	ArtworkDisplayDto getArtworkDisplayDto(Integer artworkId);
	
	void deleteArtwork(Integer artworkId, String token);
	
	List<ArtworkDisplayDto> getAllArtworkDtosSorted(String sortType);

	List<ArtworkDisplayDto> getArtworkDtosByUserSorted(Integer userId, String sortType);
	
	List<ArtworkDisplayDto> getArtworkDtosByTagSorted(String tagname, String sortType);
	
	List<ArtworkDisplayDto> searchByTitle(String keyword);
}
