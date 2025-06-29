package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.NotificationDto;
import com.example.demo.model.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByReceiverId(Integer userId);

	// 用於刪除過期通知（read_at 超過 1 天）
	//@Modifying
	//@Query("DELETE FROM Notification n WHERE n.readAt IS NOT NULL AND n.readAt < :deadline")
	//void deleteReadBefore(@Param("deadline") LocalDateTime deadline);

	@Query("""
		    SELECT new com.example.demo.model.dto.NotificationDto(
		        n.id,
		        n.messageType,
		        n.createdAt,
		        n.readAt,
		        n.admin.id
		    )
		    FROM Notification n
		    WHERE n.receiver.id = :receiverId
		    ORDER BY n.createdAt DESC
		""")
		List<NotificationDto> findNotificationDtosByReceiverId(@Param("receiverId") Integer receiverId);
	
	boolean existsByReceiverIdAndReadAtIsNull(Integer receiverId);
}
