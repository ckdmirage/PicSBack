package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.reportDto.ReportDisplayDto;
import com.example.demo.model.dto.reportDto.ReportRequestDto;
import com.example.demo.model.dto.userdto.UserCertDto;

public interface ReportService {

    // 使用者功能
    void reportArtwork(Integer artworkId, UserCertDto userCertDto, ReportRequestDto dto);
    boolean existsByReporterIdAndArtworkId(Integer reporterId, Integer artworkId);
    
    // 管理員功能
    List<ReportDisplayDto> getAllReports();
    void rejectReport(Integer reportId, UserCertDto adminDto);
    void deleteArtworkAndResolveReport(Integer reportId, UserCertDto adminDto);
}