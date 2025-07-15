package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.reportDto.ReportRequestDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ReportService;


@RestController
@RequestMapping("/report")
@CrossOrigin(origins = { "http://localhost:5173" }, allowCredentials = "true")
public class ReportRestController {

	@Autowired
    private ReportService reportService;

    // 使用者對作品檢舉（需登入）
    @PostMapping("/{artworkId}")
    public ResponseEntity<ApiResponse<String>> reportArtwork(
            @PathVariable Integer artworkId,
            @RequestBody ReportRequestDto dto,
            @RequestAttribute UserCertDto userCertDto
    ) {
        reportService.reportArtwork(artworkId, userCertDto, dto);
        return ResponseEntity.ok(ApiResponse.success("檢舉已送出", null));
    }

    // 檢查是否已檢舉 前端渲染
    @GetMapping("/check/{artworkId}")
    public ResponseEntity<ApiResponse<Boolean>> hasReported(
            @PathVariable Integer artworkId,
            @RequestAttribute UserCertDto userCertDto
    ) {
        boolean exists = reportService.existsByReporterIdAndArtworkId(userCertDto.getUserId(), artworkId);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", exists));
    }
}