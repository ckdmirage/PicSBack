package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.UnLoginException;
import com.example.demo.exception.UserException;
import com.example.demo.model.dto.EmailChangeDto;
import com.example.demo.model.dto.PasswordChangeDto;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserLoginDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;
import com.example.demo.model.dto.userdto.UserUpdateNameDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class UserRestController {
	private final UserService userService;

	// 註冊
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserRegisterDto>> addUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
		userService.addUser(userRegisterDto);
		return ResponseEntity.ok(ApiResponse.success("新增成功", userRegisterDto));
	}

	// 登入
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserCertDto>> login(@Valid @RequestBody UserLoginDto userLoginDto)
			throws UserException {
		UserCertDto userCertDto = userService.login(userLoginDto);
		return ResponseEntity.ok(ApiResponse.success("登入成功", userCertDto));
	}

	// 註冊驗證
	@GetMapping("/verify/register")
	public ResponseEntity<ApiResponse<String>> verifyUser(@RequestParam("token") String token) {
		userService.verifyUserRegister(token);
		return ResponseEntity.ok(ApiResponse.success("驗證成功!", null));
	}

	// 個人主頁
	@GetMapping("/homepage/{id}")
	public ResponseEntity<ApiResponse<UserDto>> userPage(@PathVariable Integer id) {
		UserDto userDto = userService.getUserDto(id);
		return ResponseEntity.ok(ApiResponse.success("讀取成功", userDto));
	}

	// 修改頭像
	@PostMapping("/upload/avatar")
	public ResponseEntity<ApiResponse<String>> uploadAvatar(@RequestPart("file") MultipartFile file,
			@RequestAttribute(name = "userCertDto", required = false) UserCertDto userCertDto) {
		if (userCertDto == null) {
			throw new UnLoginException("請先登入");
		}

		String imageUrl = userService.updateAvatar(file, userCertDto);
		return ResponseEntity.ok(ApiResponse.success("上傳成功", imageUrl));
	}

	// 修改用戶名
	@PutMapping("/upload/name")
	public ResponseEntity<ApiResponse<UserCertDto>> updateName(@RequestBody UserUpdateNameDto userUpdateNameDto,
			@RequestAttribute(name = "userCertDto", required = false) UserCertDto userCertDto) {
		if (userCertDto == null) {
			throw new UnLoginException("請先登入");
		}
		UserCertDto newCertDto = userService.updateName(userCertDto.getUserId(), userUpdateNameDto);
		return ResponseEntity.ok(ApiResponse.success("修改成功", newCertDto));
	}

	// 修改郵箱
	@PostMapping("/upload/email")
	public ResponseEntity<ApiResponse<String>> requestEmailChange(
			@RequestAttribute(name = "userCertDto") UserCertDto userCertDto,
			@RequestBody EmailChangeDto emailChangeDto) {
		if (userCertDto == null) {
			throw new UnLoginException("請先登入");
		}
		userService.requestEmailChange(userCertDto.getUserId(), emailChangeDto.getNewEmail());
		return ResponseEntity.ok(ApiResponse.success("新郵箱驗證信已寄出", null));
	}

	// 修改郵箱驗證
	@GetMapping("/verify/email")
	public ResponseEntity<ApiResponse<String>> verifyEmailChange(@RequestParam String token) {
		userService.verifyEmailChange(token);
		return ResponseEntity.ok(ApiResponse.success("驗證成功!", null));
	}

	// 修改密碼
	@PostMapping("/upload/password")
	public ResponseEntity<ApiResponse<String>> requestPasswordChange(
			@RequestAttribute(name = "userCertDto") UserCertDto userCertDto,
			@RequestBody PasswordChangeDto passwordChangeDto) {
		if (userCertDto == null) {
			throw new UnLoginException("請先登入");
		}
		userService.requestPasswordChange(userCertDto.getUserId(), passwordChangeDto);
		return ResponseEntity.ok(ApiResponse.success("密碼修改驗證信已寄出", null));
	}

	// 修改密碼驗證
	@GetMapping("/verify/password")
	public ResponseEntity<ApiResponse<String>> verifyPasswordChange(@RequestParam String token) {
		userService.verifyPasswordChange(token);
		return ResponseEntity.ok(ApiResponse.success("驗證成功!", null));
	}
}
