package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.artworkdto.ArtworkCardDto;
import com.example.demo.model.dto.artworkdto.ArtworkDetailDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;

public interface ArtworkService {
	
	ArtworkDetailDto uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto, MultipartFile file);
	
	ArtworkDetailDto getArtworkDetailDto(Integer artworkId, Integer currentUserId);
	
	void deleteArtwork(Integer artworkId, UserCertDto userCert);
	
	List<ArtworkCardDto> getAllArtworkDtosSorted(String sortType, Integer viewerId);

	List<ArtworkCardDto> getArtworkDtosByUserSorted(Integer userId, String sortType, Integer viewerId);
	
	List<ArtworkCardDto> getArtworkDtosByTagSorted(String tagname, String sortType, Integer viewerId);
	
	List<ArtworkCardDto> searchByTitle(String keyword, String sortType, Integer viewerId) ;
}
