package com.remotehub.notificationservice.repository;

import com.remotehub.notificationservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,UUID> {
    List<Notification> findByRecipientUsernameOrderByCreatedAtDesc(String recipientUsername);
}
