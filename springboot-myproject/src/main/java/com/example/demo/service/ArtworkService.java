package com.example.demo.service;

import com.example.demo.exception.UnLoginException;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Artwork;

public interface ArtworkService {
	Artwork uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto)throws UnLoginException;
}
