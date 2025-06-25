package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.ArtworkException;
import com.example.demo.exception.UnLoginException;
import com.example.demo.mapper.ArtworkMapper;
import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.artworkdto.ArtworkCardDto;
import com.example.demo.model.dto.artworkdto.ArtworkCardFlatDto;
import com.example.demo.model.dto.artworkdto.ArtworkDetailDto;
import com.example.demo.model.dto.artworkdto.ArtworkDetailFlatDto;
import com.example.demo.model.dto.artworkdto.ArtworkTagDto;
import com.example.demo.model.dto.artworkdto.ArtworkUploadDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.Tag;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.LikesRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ArtworkService;
import com.example.demo.service.FileStorageService;

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
	private FileStorageService fileStorageService;

	// 上傳作品
	@Override
	@Transactional
	public ArtworkDetailDto uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto,
			MultipartFile file) {
		User user = userRepository.findById(userCertDto.getUserId()).orElseThrow(() -> new UnLoginException("尚未登入!"));
		
		// 儲存圖片
		String imageUrl;
	    try {
	        imageUrl = fileStorageService.storeFile(file, "artwork");
	    } catch (IOException e) {
	        throw new RuntimeException("圖片儲存失敗: " + e.getMessage(), e);
	    }
		// Tag處理
		Set<Integer> tagIds = new HashSet<>();
		if (artworkUploadDto.getTagIds() != null && !artworkUploadDto.getTagIds().isEmpty()) {
			tagIds.addAll(artworkUploadDto.getTagIds());
		}
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

		List<Tag> allTags = tagRepository.findAllById(tagIds);
		// 創建Artwork
		Artwork artwork = new Artwork(null, user, artworkUploadDto.getTitle(), imageUrl, LocalDateTime.now(), allTags);
		Artwork saved = artworkRepository.save(artwork);
		ArtworkDetailFlatDto flat = artworkRepository.findDetailFlatById(saved.getId())
				.orElseThrow(() -> new ArtworkException("查無作品!"));
		List<ArtworkTagDto> tagTuples = artworkRepository.findTagTuplesByArtworkIds(List.of(saved.getId()));
		List<TagDto> tags = tagTuples.stream().map(t -> new TagDto(t.tagId(), t.tagName())).toList();
		return artworkMapper.toDetailDto(flat, tags, false);
	}

	// 查單筆作品
	@Override
	public ArtworkDetailDto getArtworkDetailDto(Integer artworkId, Integer currentUserId) {
		ArtworkDetailFlatDto flat = artworkRepository.findDetailFlatById(artworkId)
				.orElseThrow(() -> new ArtworkException("查無作品!"));
		List<ArtworkTagDto> tagTuples = artworkRepository.findTagTuplesByArtworkIds(List.of(artworkId));
		List<TagDto> tags = tagTuples.stream().map(t -> new TagDto(t.tagId(), t.tagName())).toList();
		boolean liked = currentUserId != null && likesRepository.existsByArtworkIdAndUserId(artworkId, currentUserId);
		// 封裝作者 UserDto
		UserDto author = new UserDto(flat.authorId(), flat.authorName(), flat.authorEmail(), flat.authorCreated(),
				flat.authorAvatarUrl());
		return new ArtworkDetailDto(flat.artworkId(), flat.title(), flat.imageUrl(), flat.uploaded(), author, tags,
				flat.likes(), liked);
	}

	// 查全部作品（支援排序）
	@Override
	public List<ArtworkCardDto> getAllArtworkDtosSorted(String sortType, Integer viewerId) {
		List<ArtworkCardFlatDto> flats = switch (sortType) {
		case "oldest" -> artworkRepository.findAllOrderByOldest();
		case "mostLiked" -> artworkRepository.findAllOrderByMostLiked();
		default -> artworkRepository.findAllOrderByNewest();
		};
		return toFullDtoList(flats, viewerId);
	}

	// 查作者作品（支援排序）
	@Override
	public List<ArtworkCardDto> getArtworkDtosByUserSorted(Integer userId, String sortType, Integer viewerId) {
		List<ArtworkCardFlatDto> flats = switch (sortType) {
		case "oldest" -> artworkRepository.findByUserIdOrderByOldest(userId);
		case "mostLiked" -> artworkRepository.findByUserIdOrderByMostLiked(userId);
		default -> artworkRepository.findByUserIdOrderByNewest(userId);
		};
		return toFullDtoList(flats, viewerId);
	}

	// 查標籤作品（支援排序）
	@Override
	public List<ArtworkCardDto> getArtworkDtosByTagSorted(String tagname, String sortType, Integer viewerId) {
		List<ArtworkCardFlatDto> flats = switch (sortType) {
		case "oldest" -> artworkRepository.findByTagNameOrderByOldest(tagname);
		case "mostLiked" -> artworkRepository.findByTagNameOrderByMostLiked(tagname);
		default -> artworkRepository.findByTagNameOrderByNewest(tagname);
		};
		if (flats.isEmpty())
			throw new ArtworkException("查無作品!");
		return toFullDtoList(flats, viewerId);
	}

	// 模糊搜尋（支援排序）
	@Override
	public List<ArtworkCardDto> searchByTitle(String keyword, String sortType, Integer viewerId) {
		List<ArtworkCardFlatDto> flats = switch (sortType) {
		case "oldest" -> artworkRepository.searchByTitleOldest(keyword);
		case "mostLiked" -> artworkRepository.searchByTitleMostLiked(keyword);
		default -> artworkRepository.searchByTitleNewest(keyword);
		};
		return toFullDtoList(flats, viewerId);
	}

	// 刪除作品
	@Override
	@Transactional
	public void deleteArtwork(Integer artworkId, UserCertDto userCert) {
		Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new ArtworkException("作品不存在!"));
		if (!artwork.getUser().getId().equals(userCert.getUserId())) {
			throw new ArtworkException("無法刪除他人的作品");
		}
		artworkRepository.delete(artwork);
	}

	// 將扁平DTO 組成前端渲染用 DTO
	private List<ArtworkCardDto> toFullDtoList(List<ArtworkCardFlatDto> flatDtos, Integer currentUserId) {
		if (flatDtos.isEmpty())
			return List.of();
		List<Integer> artworkIds = flatDtos.stream().map(ArtworkCardFlatDto::artworkId).toList();
		Set<Integer> likedArtworkIds = currentUserId == null || flatDtos.isEmpty() ? Set.of()
				: new HashSet<>(likesRepository.findLikedArtworkIds(currentUserId, artworkIds));

		List<ArtworkTagDto> tagTuples = artworkRepository.findTagTuplesByArtworkIds(artworkIds);

		// artworkId -> List<TagDto>
		Map<Integer, List<TagDto>> tagMap = new HashMap<>();
		for (ArtworkTagDto tuple : tagTuples) {
			Integer artworkId = tuple.artworkId();
			Integer tagId = tuple.tagId();
			String tagName = tuple.tagName();

			tagMap.computeIfAbsent(artworkId, k -> new ArrayList<>()).add(new TagDto(tagId, tagName));
		}

		return flatDtos.stream()
				.map(flat -> artworkMapper.toCardDto(flat, tagMap.getOrDefault(flat.artworkId(), List.of()),
						flat.likes() != null ? flat.likes().intValue() : 0, likedArtworkIds.contains(flat.artworkId())

				)).toList();
	}

}