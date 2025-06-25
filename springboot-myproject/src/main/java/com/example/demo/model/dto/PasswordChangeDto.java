package com.example.demo.model.dto;

import lombok.Data;

// 修改密碼前端回傳dto
@Data
public class PasswordChangeDto {
    private String newPassword;
    private String confirmPassword;
}
