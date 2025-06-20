package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
import com.example.demo.model.dto.TagDto;
import com.example.demo.model.dto.artworkdto.ArtworkDisplayDto;
import com.example.demo.model.dto.artworkdto.ArtworkDisplayFlatDto;
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

    // ✅ 上傳作品
    @Override
    @Transactional
    public ArtworkDisplayDto uploadArtwork(UserCertDto userCertDto, ArtworkUploadDto artworkUploadDto) {
        User user = userRepository.findById(userCertDto.getUserId())
                .orElseThrow(() -> new UnLoginException("尚未登入!"));

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
        Artwork artwork = new Artwork(null, user, artworkUploadDto.getTitle(), artworkUploadDto.getImageUrl(),
                artworkUploadDto.getUploaded(), allTags);
        artwork.setUploaded(LocalDateTime.now());
        ArtworkDisplayDto artworkDisplayDto = artworkMapper.toDisplayDto(artworkRepository.save(artwork));
        artworkDisplayDto.setLikes(likesRepository.countByArtworkId(artwork.getId()));
        return artworkDisplayDto;
    }

    // ✅ 查單筆作品詳情
    @Override
    public ArtworkDisplayDto getArtworkDisplayDto(Integer artworkId) {
        ArtworkDisplayFlatDto flat = artworkRepository.findFlatById(artworkId)
                .orElseThrow(() -> new ArtworkException("查無作品!"));

        List<Object[]> tagRows = artworkRepository.findTagTuplesByArtworkIds(List.of(artworkId));
        List<TagDto> tags = tagRows.stream()
                .map(row -> new TagDto((Integer) row[1], (String) row[2]))
                .toList();

        return artworkMapper.toDisplayDto(flat, tags);
    }

    // ✅ 查全部作品（支援排序）
    @Override
    public List<ArtworkDisplayDto> getAllArtworkDtosSorted(String sortType) {
        List<ArtworkDisplayFlatDto> flats = switch (sortType) {
            case "oldest" -> artworkRepository.findAllOrderByOldest();
            case "mostLiked" -> artworkRepository.findAllOrderByMostLiked();
            default -> artworkRepository.findAllOrderByNewest();
        };
        return toFullDtoList(flats);
    }

    // ✅ 查作者作品（支援排序）
    @Override
    public List<ArtworkDisplayDto> getArtworkDtosByUserSorted(Integer userId, String sortType) {
        List<ArtworkDisplayFlatDto> flats = switch (sortType) {
            case "oldest" -> artworkRepository.findByUserIdOrderByOldest(userId);
            case "mostLiked" -> artworkRepository.findByUserIdOrderByMostLiked(userId);
            default -> artworkRepository.findByUserIdOrderByNewest(userId);
        };
        return toFullDtoList(flats);
    }

    // ✅ 查標籤作品（支援排序）
    @Override
    public List<ArtworkDisplayDto> getArtworkDtosByTagSorted(String tagname, String sortType) {
        List<ArtworkDisplayFlatDto> flats = switch (sortType) {
            case "oldest" -> artworkRepository.findByTagNameOrderByOldest(tagname);
            case "mostLiked" -> artworkRepository.findByTagNameOrderByMostLiked(tagname);
            default -> artworkRepository.findByTagNameOrderByNewest(tagname);
        };
        if (flats.isEmpty()) throw new ArtworkException("查無作品!");
        return toFullDtoList(flats);
    }

    // ✅ 模糊搜尋（支援排序）
    @Override
    public List<ArtworkDisplayDto> searchByTitle(String keyword, String sort) {
        List<ArtworkDisplayFlatDto> flats = switch (sort) {
            case "oldest" -> artworkRepository.searchByTitleOldest(keyword);
            case "mostLiked" -> artworkRepository.searchByTitleMostLiked(keyword);
            default -> artworkRepository.searchByTitleNewest(keyword);
        };
        return toFullDtoList(flats);
    }

    // ✅ 刪除作品
    @Override
    @Transactional
    public void deleteArtwork(Integer artworkId, String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        User user = userRepository.findById(jwtUtil.extractUserId(token))
                .orElseThrow(() -> new UnLoginException("用戶未登入!"));

        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ArtworkException("作品不存在!"));

        if (!artwork.getUser().getId().equals(user.getId())) {
            throw new ArtworkException("無法刪除他人的作品");
        }
        artworkRepository.delete(artwork);
    }

    // ✅ 將扁平 DTO 組成完整 DTO（含作者、tag、likes）
    private List<ArtworkDisplayDto> toFullDtoList(List<ArtworkDisplayFlatDto> flatDtos) {
        if (flatDtos.isEmpty()) return List.of();

        List<Integer> ids = flatDtos.stream().map(ArtworkDisplayFlatDto::artworkId).toList();
        List<Object[]> tagRows = artworkRepository.findTagTuplesByArtworkIds(ids);

        Map<Integer, List<TagDto>> tagMap = new HashMap<>();
        for (Object[] row : tagRows) {
            Integer artworkId = (Integer) row[0];
            Integer tagId = (Integer) row[1];
            String tagName = (String) row[2];
            tagMap.computeIfAbsent(artworkId, k -> new ArrayList<>())
                  .add(new TagDto(tagId, tagName));
        }

        return flatDtos.stream().map(flat ->
                artworkMapper.toDisplayDto(flat, tagMap.getOrDefault(flat.artworkId(), List.of()))
        ).toList();
    }
}