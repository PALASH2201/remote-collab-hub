package com.remotehub.notificationservice.service;

import com.remotehub.notificationservice.entity.CommentAddedEvent;
import com.remotehub.notificationservice.entity.Notification;
import com.remotehub.notificationservice.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {
    private final NotificationRepository notificationRepository;

    public NotificationListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @RabbitListener(queues = "comment.added.queue")
    public void handleCommentAdded(CommentAddedEvent event) {
        // Here you’d call ProjectService + UserService to find recipients.
        // For demo, let’s notify a fixed user.
        Notification notif = new Notification();
        notif.setRecipientId("user_lmn012");
        notif.setType("NEW_COMMENT");
        notif.setMessage("New comment added on entity " + event.getEntityId());
        notif.setLink("/projects/" + event.getEntityId());
        notificationRepository.save(notif);
    }
}

