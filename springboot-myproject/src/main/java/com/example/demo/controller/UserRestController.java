package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.UserException;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/User")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8002" }, allowCredentials = "true")
public class UserRestController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserDto>> addUser(@Valid@RequestBody UserDto userDto, BindingResult bindingResult){
		//if(bindingResult.hasErrors()) {
		//	throw new UserException("新增失敗:"+bindingResult.getAllErrors().get(0).getDefaultMessage());
		//}
		//userService.addUser(userDto.getUserId(),);
		return null;
	}
}
