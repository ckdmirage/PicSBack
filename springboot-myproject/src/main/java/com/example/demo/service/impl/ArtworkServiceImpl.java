package com.example.demo.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.example.demo.model.entity.Tag;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ArtworkService;
import com.example.demo.util.JwtUtil;

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
	private ArtworkMapper artworkMapper;
	
	@Autowired
	private JwtUtil jwtUtil;

	// 上傳作品
	@Override
	@Transactional
	public Artwork uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto) {
	    User user = userRepository.findById(userCertDto.getUserId())
	        .orElseThrow(() -> new UnLoginException("尚未登入!"));

	    Set<Integer> tagIds = new HashSet<>(); // 1. 用 Set 去重

	    // 2. 舊 tag id 加進去
	    if (artworkUploadDto.getTagIds() != null && !artworkUploadDto.getTagIds().isEmpty()) {
	        tagIds.addAll(artworkUploadDto.getTagIds());
	    }

	    // 3. 新 tag name 查重建新，然後加進 set
	    if (artworkUploadDto.getNewTagnames() != null) {
	        for (String name : artworkUploadDto.getNewTagnames()) {
	            Optional<Tag> optTag = tagRepository.getTag(name);
	            if (optTag.isPresent()) {
	                tagIds.add(optTag.get().getId());
	            } else {
	                Tag newTag = new Tag();
	                newTag.setName(name);
	                Tag savedTag = tagRepository.save(newTag);
	                tagIds.add(savedTag.getId());
	            }
	        }
	    }

	    // 4. 只用 attached entity，查回所有要關聯的 tag
	    List<Tag> allTags = tagRepository.findAllById(tagIds);

	    // 5. 存作品
	    Artwork artwork = new Artwork(
	        null,
	        user,
	        artworkUploadDto.getTitle(),
	        artworkUploadDto.getImageUrl(),
	        artworkUploadDto.getUploaded(),
	        allTags
	    );
	    return artworkRepository.save(artwork);
	}



	// 獲取單個作品
	@Override
	public ArtworkDisplayDto getArtworkDisplayDto(Integer artworkId) {
		Optional<Artwork> optArtwork = artworkRepository.findByIdWithTags(artworkId);
		Artwork artwork = optArtwork.orElseThrow(() -> new ArtworkException("查無作品!"));
		User user = artwork.getUser();
		ArtworkDisplayDto artworkDisplayDto = artworkMapper.toDisplayDto(artwork);
		artworkDisplayDto.setAuthorId(user.getId());
		return artworkDisplayDto;
	}

	// 獲取多個作品
	@Override
	public List<ArtworkDisplayDto> getArtworksDisplayDto(Integer userId) {
		List<Artwork> artworks = artworkRepository.findByUserId(userId);
		if (artworks.isEmpty()) {
			throw new ArtworkException("查無作品!");
		}
		return artworks.stream().map(artworkMapper::toDisplayDto).collect(Collectors.toList());
	}


	//刪除作品
	@Override
	@Transactional
	public void deleteArtwork(Integer artworkId, String token) {
		if (token.startsWith("Bearer ")) {
	        token = token.substring(7);
	    }
		User user = userRepository.findById(jwtUtil.extractUserId(token)).orElseThrow(()-> new UnLoginException("用戶未登入!"));
		
		Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(()->new ArtworkException("作品不存在!"));
		
		if(!artwork.getUser().getId().equals(user.getId())) {
			throw new ArtworkException("無法刪除他人的作品");
		}
		artworkRepository.delete(artwork);
	}

}
