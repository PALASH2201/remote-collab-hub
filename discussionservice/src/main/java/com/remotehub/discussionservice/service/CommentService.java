package com.remotehub.discussionservice.service;

import com.remotehub.discussionservice.entity.Comment;
import com.remotehub.discussionservice.entity.CommentAddedEvent;
import com.remotehub.discussionservice.repository.CommentRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final RabbitTemplate rabbitTemplate;

    public CommentService(CommentRepository commentRepository, RabbitTemplate rabbitTemplate) {
        this.commentRepository = commentRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Comment addComment(Comment comment) {
        Comment saved = commentRepository.save(comment);

        CommentAddedEvent event = new CommentAddedEvent();
        event.setCommentId(saved.getId());
        event.setAuthorId(saved.getAuthorId());
        event.setEntityId(saved.getEntityId());
        event.setEntityType(saved.getEntityType());
        event.setParentId(saved.getParentId());

        rabbitTemplate.convertAndSend("discussion.exchange", "comment.added", event);
        return saved;
    }
}

