package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.response.ApiResponse;

//全域例外處理

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 使用者沒登入
    @ExceptionHandler(UnLoginException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnLogin(UnLoginException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, e.getMessage()));
    }

    // 2. 使用者不存在
    @ExceptionHandler(UserNoFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNoFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, e.getMessage()));
    }

    // 3. 註冊錯誤（帳號重複、驗證碼錯誤等）
    @ExceptionHandler(UserRegisterException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserRegister(UserRegisterException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }

    // 4. 一般用戶錯誤
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<Object>> handleUser(UserException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }

    // 5. 追蹤錯誤（已追蹤、自己不能追蹤自己等）
    @ExceptionHandler(FollowException.class)
    public ResponseEntity<ApiResponse<Object>> handleFollow(FollowException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }

    // 6. 按讚錯誤（已按讚、沒按讚等）
    @ExceptionHandler(LikesException.class)
    public ResponseEntity<ApiResponse<Object>> handleLikes(LikesException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }

    // 7. 作品處理錯誤
    @ExceptionHandler(ArtworkException.class)
    public ResponseEntity<ApiResponse<Object>> handleArtwork(ArtworkException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }

    // 8. 標籤錯誤
    @ExceptionHandler(TagException.class)
    public ResponseEntity<ApiResponse<Object>> handleTag(TagException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }

    // 9. 密碼錯誤（登入時密碼錯）
    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ApiResponse<Object>> handlePasswordInvalid(PasswordInvalidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }
    // 10 更新帳號信息錯誤
    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserUpdate(UserUpdateException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }
    
    // 兜底處理其他未知錯誤
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleOther(Exception e) {
        e.printStackTrace(); // 建議印出 log 協助除錯
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "伺服器發生錯誤，請稍後再試"));
    }
}