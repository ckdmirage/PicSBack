package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ArtworkException;
import com.example.demo.exception.UnLoginException;
import com.example.demo.mapper.ArtworkMapper;
import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.Tag;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.LikesRepository;
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
	private LikesRepository likesRepository;

	@Autowired
	private ArtworkMapper artworkMapper;

	@Autowired
	private JwtUtil jwtUtil;

	// 上傳作品
	@Override
	@Transactional
	public ArtworkDisplayDto uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto) {
		User user = userRepository.findById(userCertDto.getUserId()).orElseThrow(() -> new UnLoginException("尚未登入!"));
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
		Artwork artwork = new Artwork(null, user, artworkUploadDto.getTitle(), artworkUploadDto.getImageUrl(),
				artworkUploadDto.getUploaded(), allTags);
		artwork.setUploaded(LocalDateTime.now());
		ArtworkDisplayDto artworkDisplayDto = artworkMapper.toDisplayDto(artworkRepository.save(artwork));
		artworkDisplayDto.setLikes(likesRepository.countByArtworkId(artwork.getId()));
		return artworkDisplayDto;
	}

	// 獲取單個作品
	@Override
	public ArtworkDisplayDto getArtworkDisplayDto(Integer artworkId) {
		Optional<Artwork> optArtwork = artworkRepository.findByIdWithTags(artworkId);
		Artwork artwork = optArtwork.orElseThrow(() -> new ArtworkException("查無作品!"));
		User user = artwork.getUser();
		ArtworkDisplayDto artworkDisplayDto = artworkMapper.toDisplayDto(artwork);
		artworkDisplayDto.setAuthorId(user.getId());
		artworkDisplayDto.setAuthorname(user.getUsername());
		artworkDisplayDto.setLikes(likesRepository.countByArtworkId(artworkId));
		return artworkDisplayDto;
	}

	// 獲取所有作品
	@Override
	public List<ArtworkDisplayDto> getAllArtworkDtosSorted(String sortType) {
		List<Artwork> artworks = artworkRepository.findAllWithTagsUser();
		return switch (sortType) {
		    case "oldest" -> toDtoWithLikes(
		        artworks.stream()
		            .sorted(Comparator.comparing(Artwork::getUploaded))
		            .collect(Collectors.toList())
		    );
		    case "mostLiked" -> toDtoWithLikesSortedByLikes(artworks);
		    default -> toDtoWithLikes(
		        artworks.stream()
		            .sorted(Comparator.comparing(Artwork::getUploaded).reversed())
		            .collect(Collectors.toList())
		    );
		};
	}

	// 獲取作者作品表
	@Override
	public List<ArtworkDisplayDto> getArtworkDtosByUserSorted(Integer userId, String sortType) {
		List<Artwork> artworks = artworkRepository.findByUserId(userId);
		if (artworks.isEmpty())
			throw new ArtworkException("查無作品!");

		return switch (sortType) {
		case "mostLiked" -> toDtoWithLikesSortedByLikes(artworks);
		case "oldest" -> toDtoWithLikes(artworks.stream().sorted(Comparator.comparing(Artwork::getUploaded))
				.collect(Collectors.toList()));
		default -> toDtoWithLikes(artworks.stream()
				.sorted(Comparator.comparing(Artwork::getUploaded).reversed()).collect(Collectors.toList()));
		};
	}

	// 獲取標籤作品表
	@Override
	public List<ArtworkDisplayDto> getArtworkDtosByTagSorted(String tagname, String sortType) {
		List<Artwork> artworks = artworkRepository.findByTagName(tagname);
		if (artworks.isEmpty())
			throw new ArtworkException("查無作品!");

		return switch (sortType) {
		case "mostLiked" -> toDtoWithLikesSortedByLikes(artworks);
		case "oldest" -> toDtoWithLikes(artworks.stream().sorted(Comparator.comparing(Artwork::getUploaded))
				.collect(Collectors.toList()));
		default -> toDtoWithLikes(artworks.stream()
				.sorted(Comparator.comparing(Artwork::getUploaded).reversed()).collect(Collectors.toList()));
		};
	}

	//dtos添加Author和like信息
	private List<ArtworkDisplayDto> toDtoWithLikes(List<Artwork> artworks) {
		if (artworks.isEmpty()) {
			return List.of();
		}
		List<Integer> ids = artworks.stream().map(Artwork::getId).toList();

		Map<Integer, Integer> likeMap = new HashMap<>();
		for (Object[] row : likesRepository.countLikesByArtworkIds(ids)) {
			likeMap.put((Integer) row[0], ((Long) row[1]).intValue());
		}

		return artworks.stream().map(artwork -> {
			ArtworkDisplayDto dto = artworkMapper.toDisplayDto(artwork);
			dto.setLikes(likeMap.getOrDefault(artwork.getId(), 0));
			return dto;
		}).collect(Collectors.toList());
	}

	// 控制排序方法

	private List<ArtworkDisplayDto> toDtoWithLikesSortedByLikes(List<Artwork> artworks) {
		// 1. 先包成包含 likes 的 DTO 列表
		List<ArtworkDisplayDto> list = toDtoWithLikes(artworks);

		// 2. 按 likes 排序（從多到少）
		return list.stream().sorted(Comparator.comparing(ArtworkDisplayDto::getLikes).reversed())
				.collect(Collectors.toList());
	}

	// 刪除作品
	@Override
	@Transactional
	public void deleteArtwork(Integer artworkId, String token) {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		User user = userRepository.findById(jwtUtil.extractUserId(token))
				.orElseThrow(() -> new UnLoginException("用戶未登入!"));

		Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new ArtworkException("作品不存在!"));

		if (!artwork.getUser().getId().equals(user.getId())) {
			throw new ArtworkException("無法刪除他人的作品");
		}
		artworkRepository.delete(artwork);
	}

	//模糊搜索
	@Override
	public List<ArtworkDisplayDto> searchByTitle(String keyword) {
		List <Artwork> artworks =  artworkRepository.findByTitleContainingIgnoreCase(keyword);
		 return toDtoWithLikes(artworks);
	}
}
