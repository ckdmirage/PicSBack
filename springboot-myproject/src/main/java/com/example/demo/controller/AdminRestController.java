package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.reportDto.ReportDisplayDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserManageDto;
import com.example.demo.model.dto.userdto.UserUpgradeRoleDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ReportService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = { "http://localhost:5173" }, allowCredentials = "true")
public class AdminRestController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private UserService userService;

	// 查詢所有未處理檢舉
	@GetMapping("/artwork/list")
	public ResponseEntity<ApiResponse<List<ReportDisplayDto>>> getAllPendingReports() {
		return ResponseEntity.ok(ApiResponse.success("查詢成功", reportService.getAllReports()));
	}

	// 駁回檢舉（需為管理員）
	@PostMapping("/reject/{reportId}")
	public ResponseEntity<ApiResponse<String>> rejectReport(@PathVariable Integer reportId,
			@RequestAttribute UserCertDto userCertDto 
	) {
		reportService.rejectReport(reportId, userCertDto);
		return ResponseEntity.ok(ApiResponse.success("已駁回檢舉", null));
	}

	// 通過檢舉並刪除作品
	@PostMapping("/approve/{reportId}")
	public ResponseEntity<ApiResponse<String>> approveAndDeleteArtwork(@PathVariable Integer reportId,
			@RequestAttribute UserCertDto userCertDto 
	) {
		reportService.deleteArtworkAndResolveReport(reportId, userCertDto);
		return ResponseEntity.ok(ApiResponse.success("作品已刪除", null));
	}

	// 查詢用戶列表
	@GetMapping("/user/list")
	public ResponseEntity<ApiResponse<Page<UserManageDto>>> getAllUsers(@RequestAttribute UserCertDto userCertDto,Pageable pageable) {
		Page<UserManageDto> users = userService.getAllUsersForAdmin(userCertDto, pageable);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", users));
	}

	// 管理員更新用戶權限
	@PutMapping("/user/{targetId}/role")
	public ResponseEntity<ApiResponse<String>> updateUserRole(@PathVariable Integer targetId,
			@RequestBody UserUpgradeRoleDto request, @RequestAttribute UserCertDto userCertDto) {
		userService.updateUserRole(targetId, request.getNewRole(), userCertDto);
		return ResponseEntity.ok(ApiResponse.success("角色更新成功", null));
	}

}
