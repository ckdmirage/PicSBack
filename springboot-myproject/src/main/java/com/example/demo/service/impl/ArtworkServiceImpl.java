package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ArtworkException;
import com.example.demo.exception.UnLoginException;
import com.example.demo.mapper.ArtworkMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ArtworkService;

import jakarta.transaction.Transactional;

@Service
public class ArtworkServiceImpl implements ArtworkService {
	@Autowired
	private ArtworkRepository artworkRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ArtworkMapper artworkMapper;

	//上傳作品
	@Override
	public Artwork uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto){
		User user = userRepository.getUser(userCertDto.getUsername()).orElseThrow(() -> new UnLoginException("尚未登入!"));
		
		Artwork artwork = new Artwork(null, user, artworkUploadDto.getTitle(), artworkUploadDto.getImageUrl(),
				artworkUploadDto.getUploaded(), tagRepository.findAllById(artworkUploadDto.getTagIds()));	
		return artworkRepository.save(artwork);
	}
	
	//獲取單個作品
	@Override
	@Transactional
	public ArtworkDisplayDto getArtworkDisplayDto(Integer artworkId) {
		Optional<Artwork> optArtwork = artworkRepository.findById(artworkId);
		Artwork artwork = optArtwork.orElseThrow(()-> new ArtworkException("查無作品!"));
		User user = artwork.getUser();
		ArtworkDisplayDto artworkDisplayDto = artworkMapper.toDisplayDto(artwork);
		artworkDisplayDto.setAuthorUsername(user.getUsername());
		return artworkDisplayDto;
	}
	
	
	//獲取多個作品
	@Override
	public List<ArtworkDisplayDto> getArtworksDisplayDto(Integer userId) {
		return null;
		
	}
	
	
	
	
	
}
