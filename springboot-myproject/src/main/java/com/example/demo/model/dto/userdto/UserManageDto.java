package com.example.demo.model.dto.userdto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

//管理用扁平化dto
@Data
@AllArgsConstructor
public class UserManageDto {
    private Integer userId;
    private String username;
    private String role;
    private String avatarUrl;
    private LocalDateTime createdAt;
}