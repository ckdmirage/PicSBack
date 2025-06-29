package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.reportDto.ReportDisplayDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ReportService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = { "http://localhost:5173" }, allowCredentials = "true")
public class AdminController {

	@Autowired
    private ReportService reportService;

    // 查詢所有未處理檢舉
    @GetMapping("/artwork/list")
    public ResponseEntity<ApiResponse<List<ReportDisplayDto>>> getAllPendingReports() {
        return ResponseEntity.ok(ApiResponse.success("查詢成功", reportService.getAllReports()));
    }

    // 駁回檢舉（需為管理員）
    @PostMapping("/reject/{reportId}")
    public ResponseEntity<ApiResponse<String>> rejectReport(
            @PathVariable Integer reportId,
            @RequestAttribute UserCertDto userCertDto   // ✅ 接收從 JwtFilter 傳進來的 admin 身分
    ) {
        reportService.rejectReport(reportId, userCertDto);
        return ResponseEntity.ok(ApiResponse.success("已駁回檢舉", null));
    }

    // 通過檢舉並刪除作品
    @PostMapping("/approve/{reportId}")
    public ResponseEntity<ApiResponse<String>> approveAndDeleteArtwork(
            @PathVariable Integer reportId,
            @RequestAttribute UserCertDto userCertDto   // ✅ 傳給 Service 用來記錄誰發的通知
    ) {
        reportService.deleteArtworkAndResolveReport(reportId, userCertDto);
        return ResponseEntity.ok(ApiResponse.success("作品已刪除", null));
    }
}
