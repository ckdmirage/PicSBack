package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.UnLoginException;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ArtworkService;

@Service
public class ArtworkServiceImpl implements ArtworkService {
	@Autowired
	private ArtworkRepository artworkRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TagRepository tagRepository;

	//上傳作品
	@Override
	public Artwork uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto) throws UnLoginException {
		User user = userRepository.getUser(userCertDto.getUsername()).orElseThrow(() -> new UnLoginException("尚未登入!"));
		
		Artwork artwork = new Artwork(null, user, artworkUploadDto.getTitle(), artworkUploadDto.getImageUrl(),
				artworkUploadDto.getUploaded(), tagRepository.findAllById(artworkUploadDto.getTagIds()));	
		return artworkRepository.save(artwork);
	}
}
