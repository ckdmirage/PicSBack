package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.ArtworkException;
import com.example.demo.exception.ReportException;
import com.example.demo.exception.UserNoFoundException;
import com.example.demo.model.dto.reportDto.ReportDisplayDto;
import com.example.demo.model.dto.reportDto.ReportRequestDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.entity.Artwork;
import com.example.demo.model.entity.Report;
import com.example.demo.model.entity.Tag;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.NotificationMessageType;
import com.example.demo.repository.ArtworkRepository;
import com.example.demo.repository.FavouriteRepository;
import com.example.demo.repository.LikesRepository;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.NotificationService;
import com.example.demo.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ArtworkRepository artworkRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FavouriteRepository favouriteRepository;

	@Autowired
	private LikesRepository likesRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private NotificationService notificationService;

	// 檢舉作品
	@Override
	@Transactional
	public void reportArtwork(Integer artworkId, UserCertDto userCertDto, ReportRequestDto dto) {

		Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new ArtworkException("作品不存在"));

		// 防止自我檢舉
		if (artwork.getUser().getId().equals(userCertDto.getUserId())) {
			throw new ReportException("不能檢舉自己的作品");
		}

		// 防止重複檢舉
		boolean hasReported = reportRepository.existsByReporterIdAndArtworkId(userCertDto.getUserId(), artworkId);
		if (hasReported) {
			throw new ReportException("你已經檢舉過該作品");
		}

		// 發出新檢舉
		User reporter = userRepository.findById(userCertDto.getUserId())
				.orElseThrow(() -> new UserNoFoundException("用戶不存在"));

		Report report = new Report();
		report.setReporter(reporter);
		report.setArtwork(artwork);
		report.setReason(dto.getReason());
		report.setReportedAt(LocalDateTime.now());

		reportRepository.save(report);
	}

	// 檢查檢舉-前端渲染用
	@Override
	public boolean existsByReporterIdAndArtworkId(Integer reporterId, Integer artworkId) {
		return reportRepository.existsByReporterIdAndArtworkId(reporterId, artworkId);
	}

	// 獲取檢舉列表-前端後台頁面
	@Override
	public List<ReportDisplayDto> getAllReports() {
		return reportRepository.findAllReports();
	}

	// 檢舉操作-駁回檢舉
	@Override
	@Transactional
	public void rejectReport(Integer reportId, UserCertDto adminDto) {
		Report report = reportRepository.findById(reportId).orElseThrow(() -> new RuntimeException("檢舉紀錄不存在"));

		User admin = userRepository.findById(adminDto.getUserId()).orElseThrow(() -> new RuntimeException("非管理員身份"));
		// 發送通知
		notificationService.sendNotification(admin, report.getReporter(), NotificationMessageType.REPORT_REJECTED);

		reportRepository.delete(report);

	}

	// 檢舉操作-通過檢舉，刪除作品
	@Override
	@Transactional
	public void deleteArtworkAndResolveReport(Integer reportId, UserCertDto adminDto) {
		Report report = reportRepository.findById(reportId).orElseThrow(() -> new RuntimeException("檢舉紀錄不存在"));

		User admin = userRepository.findById(adminDto.getUserId()).orElseThrow(() -> new RuntimeException("非管理員身份"));

		Artwork artwork = artworkRepository.findById(report.getArtwork().getId())
				.orElseThrow(() -> new RuntimeException("作品不存在"));

		// 刪除作品的所有關聯關係
		List<Integer> tagIds = artwork.getTags().stream().map(Tag::getId).collect(Collectors.toList());
		
		reportRepository.delete(report);
		reportRepository.flush();
		
		for (Tag tag : artwork.getTags()) {
		    tag.getArtworks().remove(artwork);
		}
		artwork.getTags().clear();
		artworkRepository.saveAndFlush(artwork); // 更新中介表

		likesRepository.deleteByArtworkId(artwork.getId());
		favouriteRepository.deleteAllByArtworkId(artwork.getId());

		artworkRepository.delete(artwork);
		
		for (Integer tagId : tagIds) {
		    if (tagRepository.countArtworkByTagId(tagId) == 0) {
		        tagRepository.deleteById(tagId);
		    }
		}
		
		// 發送通知
		notificationService.sendNotification(admin, report.getReporter(), NotificationMessageType.REPORT_APPROVED);
		notificationService.sendNotification(admin, artwork.getUser(), NotificationMessageType.ARTWORK_REMOVED);

	}

}
