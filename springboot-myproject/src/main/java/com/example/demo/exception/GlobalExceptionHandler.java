package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.response.ApiResponse;

//全域例外處理

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UserNoFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNoFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, e.getMessage()));
    }
}
