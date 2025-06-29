package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.NotificationDto;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.NotificationMessageType;

public interface NotificationService {
    void sendNotification(User admin, User receiver, NotificationMessageType type);

    List<NotificationDto> getNotificationsForUser(Integer userId);

    void markAsRead(Long notificationId);

    //void cleanupOldNotificationsForUser(Integer userId);
    
    boolean hasUnread(Integer userId);
}