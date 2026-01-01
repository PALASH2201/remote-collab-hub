package com.remotehub.notificationservice.service;

import com.remotehub.notificationservice.entity.CommentAddedEvent;
import com.remotehub.notificationservice.entity.Notification;
import com.remotehub.notificationservice.feign.ProjectInterface;
import com.remotehub.notificationservice.feign.DiscussionInterface;
import com.remotehub.notificationservice.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class NotificationListener {

    @Value("${task.url}")
    private String taskUrl;
    @Value("${project.url}")
    private String projectUrl;
    @Value("${sprint.url}")
    private String sprintUrl;
    private final NotificationRepository notificationRepository;
    private final ProjectInterface projectInterface;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DiscussionInterface discussionInterface;

    public NotificationListener(DiscussionInterface discussionInterface,SimpMessagingTemplate simpMessagingTemplate, NotificationRepository notificationRepository, ProjectInterface projectInterface) {
        this.notificationRepository = notificationRepository;
        this.projectInterface = projectInterface;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.discussionInterface = discussionInterface;
    }

    @RabbitListener(queues = "comment.added.queue")
    public void handleCommentAdded(CommentAddedEvent event) {
        try{
            String authorUsername = event.getAuthorUsername();
            Set<String> recipients = new HashSet<>();

            if (event.getMentions().size() == 1 && event.getMentions().getFirst().equals("everyone")) {
                ResponseEntity<List<String>> resp = projectInterface.getProjectMembers(event.getEntityId());
                log.info("Response: {}",resp);
                recipients.addAll(resp.getBody());
            } else{
                recipients.addAll(event.getMentions());
            }
            
            if(event.getParentId() != null){
                String parentCommentAuthorUsername = discussionInterface.getParentCommentAuthorUsername(event.getParentId()).getBody();
                recipients.add(parentCommentAuthorUsername);
            }
            
            recipients.remove(authorUsername);

            for (String username : recipients) {
                Notification notif = new Notification();
                notif.setRecipientUsername(username);
                if(event.getParentId() != null) notif.setType("REPLY_TO_OLD_COMMENT");
                else notif.setType("NEW_COMMENT_ADDED");
                notif.setMessage(event.getContent());
                notif.setLink(buildLink(event));
                Notification saved = notificationRepository.save(notif);

                simpMessagingTemplate.convertAndSendToUser(
                        username,
                        "/queue/notifications",
                        saved
                );
            }
        } catch (Exception e) {
            log.error("Error : {} ", e.getMessage());
        }
    }

    public String buildLink(CommentAddedEvent event){
        if(event.getEntityType().equals("TASK")){
            return taskUrl;
        }else if(event.getEntityType().equals("SPRINT")){
            return sprintUrl;
        }else{
            return projectUrl;
        }
    }

}

