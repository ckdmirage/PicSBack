package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
	private int status; 
	private String message;// 訊息
	T data;// 實際資料

	// 成功回應
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<T>(200, message, data);
	}

	// 失敗回應
	public static <T> ApiResponse<T> error(int status, String message) {
		return new ApiResponse<T>(status, message, null);
	}
}