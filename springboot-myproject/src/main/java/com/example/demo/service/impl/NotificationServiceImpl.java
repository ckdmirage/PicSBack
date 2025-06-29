	package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.NotificationException;
import com.example.demo.model.dto.NotificationDto;
import com.example.demo.model.entity.Notification;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.NotificationMessageType;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService{

	@Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;
	
    //發送通知
	@Override	
	@Transactional
	public void sendNotification(User admin, User receiver, NotificationMessageType type) {
		if (admin == null || receiver == null || type == null) {
		    throw new NotificationException("通知發送參數不能為 null");
		}
		
		Notification notification = Notification.builder()
                .admin(admin)
                .receiver(receiver)
                .messageType(type)
                .createdAt(LocalDateTime.now())
                .build();
		notificationRepository.save(notification);
	}

	//用戶檢查通知列表
	@Override
	public List<NotificationDto> getNotificationsForUser(Integer userId) {
		 return notificationRepository.findNotificationDtosByReceiverId(userId);
	}

	// 標記已讀-刪除通知用
	@Override
	public void markAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException("未找到通知"));
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
	}

	//刪除通知
	//@Override
	//public void cleanupOldNotificationsForUser(Integer userId) {
	//	 LocalDateTime deadline = LocalDateTime.now().minusDays(1);
	//        notificationRepository.deleteReadBefore(deadline);
	//}

	//檢查未讀-前端渲染用
	@Override
	public boolean hasUnread(Integer userId) {
		return notificationRepository.existsByReceiverIdAndReadAtIsNull(userId);
	}
	
}
