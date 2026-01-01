package com.remotehub.discussionservice.service;

import com.remotehub.discussionservice.entity.Comment;
import com.remotehub.discussionservice.entity.CommentAddedEvent;
import com.remotehub.discussionservice.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final RabbitTemplate rabbitTemplate;

    public CommentService(CommentRepository commentRepository, RabbitTemplate rabbitTemplate) {
        this.commentRepository = commentRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Comment addComment(Comment comment) {
        Comment saved = commentRepository.save(comment);

        String authHeader = null;
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            authHeader = attrs.getRequest().getHeader("Authorization");
        }

        CommentAddedEvent event = new CommentAddedEvent();
        event.setCommentId(String.valueOf(saved.getId()));
        event.setAuthorId(saved.getAuthorId());
        event.setEntityId(saved.getEntityId());
        event.setEntityType(saved.getEntityType());
        event.setParentId(String.valueOf(saved.getParentId()));
        event.setMentions(saved.getMentions());
        event.setContent(saved.getContent());
        event.setAuthHeader(authHeader);

        rabbitTemplate.convertAndSend("discussion.exchange", "comment.added", event);
        return saved;
    }

    public Comment getComment(String parentId) {
        try{
            ObjectId objectId = new ObjectId(parentId);
            return commentRepository.findById(objectId)
                    .orElseThrow(RuntimeException::new);
        } catch (Exception e){
            log.error("Error : {}" , e.getMessage());
            return null;
        }
    }
}

