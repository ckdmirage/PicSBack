package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.UserException;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.dto.userdto.UserLoginDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class UserRestController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserRegisterDto>> addUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
	    userService.addUser(userRegisterDto);
	    return ResponseEntity.ok(ApiResponse.success("新增成功", userRegisterDto));
	}

	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserCertDto>> login(@Valid @RequestBody UserLoginDto userLoginDto) throws UserException {
	    UserCertDto userCertDto = userService.login(userLoginDto);
	    return ResponseEntity.ok(ApiResponse.success("登入成功", userCertDto));
	}
	
	//郵箱驗證
	@GetMapping("/verify")
	public ResponseEntity<ApiResponse<String>> verifyUser(@RequestParam("token") String token){
		boolean isVerified = userService.verifyUserToken(token);
		if (isVerified) {
	        return ResponseEntity.ok(ApiResponse.success("驗證成功", null));
	    } else {
	        return ResponseEntity.badRequest().body(ApiResponse.error(400, "驗證失敗或連結過期"));
	    }
	}
	
	//個人主頁
	@GetMapping("/homepage/{id}")
	public ResponseEntity<ApiResponse<UserDto>> userPage(@PathVariable Integer id){
		UserDto userDto = userService.getUserDto(id);
		return ResponseEntity.ok(ApiResponse.success("讀取成功", userDto));
	}
	

}
