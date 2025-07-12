package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.artworkdto.ArtworkCardDto;
import com.example.demo.model.dto.artworkdto.ArtworkDetailDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;

public interface ArtworkService {
	
	ArtworkDetailDto uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto, MultipartFile file);
	
	ArtworkDetailDto getArtworkDetailDto(Integer artworkId, Integer currentUserId);
	
	void deleteArtwork(Integer artworkId, UserCertDto userCert);
	
	Page<ArtworkCardDto> getAllArtworkDtosPaged(Pageable pageable, Integer viewerId);

	Page<ArtworkCardDto> getArtworkDtosByUserPaged(Integer userId, Pageable pageable, Integer viewerId);
	
	Page<ArtworkCardDto> getArtworkDtosByTagPaged(String tagname, Pageable pageable, Integer viewerId);
	
	Page<ArtworkCardDto> searchByTitle(String keyword, Pageable pageable, Integer viewerId);
}
