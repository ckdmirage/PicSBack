package com.example.demo.model.entity;

import java.time.LocalDateTime;

import com.example.demo.model.enums.NotificationMessageType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 通知接收者：可能是檢舉者或被檢舉的作者
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // 發送者，一定是管理員
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    // 通知內容（固定字串，使用 Enum）
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private NotificationMessageType messageType;

    // 管理員處理時間（通知建立時間）
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 用戶閱讀時間
    @Column(name = "read_at")
    private LocalDateTime readAt;
}