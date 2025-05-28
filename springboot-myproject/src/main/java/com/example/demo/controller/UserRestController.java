package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.UserException;
import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserLoginDto;
import com.example.demo.model.dto.userdto.UserRegisterDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class UserRestController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserRegisterDto>> addUser(@Valid@RequestBody UserRegisterDto userRegisterDto, BindingResult bindingResult) throws UserException{
		if(bindingResult.hasErrors()) {
			throw new UserException("新增失敗:"+bindingResult.getAllErrors().get(0).getDefaultMessage());
		}
		userService.addUser(userRegisterDto);
		return ResponseEntity.ok(ApiResponse.success("新增成功", userRegisterDto));
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserCertDto>> login(@Valid@RequestBody UserLoginDto userLoginDto, BindingResult bindingResult) throws UserException{
		if(bindingResult.hasErrors()) {
			throw new UserException("登入失敗:"+bindingResult.getAllErrors().get(0).getDefaultMessage());
		}
		UserCertDto userCertDto =  userService.login(userLoginDto);
		return ResponseEntity.ok(ApiResponse.success("登入成功", userCertDto));
		
	}
	
	@GetMapping("/verify")
	public ResponseEntity<ApiResponse<String>> verifyUser(@RequestParam("token") String token){
		boolean isVerified = userService.verifyUserToken(token);
		if (isVerified) {
	        return ResponseEntity.ok(ApiResponse.success("驗證成功", null));
	    } else {
	        return ResponseEntity.badRequest().body(ApiResponse.error(400, "驗證失敗或連結過期"));
	    }
	}
}
