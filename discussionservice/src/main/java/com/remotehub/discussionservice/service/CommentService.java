package com.remotehub.discussionservice.service;

import com.remotehub.discussionservice.dto.mapper.CommentMapper;
import com.remotehub.discussionservice.dto.request.CommentRequestDto;
import com.remotehub.discussionservice.dto.response.CommentResponseDto;
import com.remotehub.discussionservice.entity.Comment;
import com.remotehub.discussionservice.entity.CommentAddedEvent;
import com.remotehub.discussionservice.exceptions.ErrorCreatingEntry;
import com.remotehub.discussionservice.exceptions.ResourceNotFoundException;
import com.remotehub.discussionservice.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper, CommentRepository commentRepository, RabbitTemplate rabbitTemplate) {
        this.commentRepository = commentRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.commentMapper = commentMapper;
    }

    public CommentResponseDto addComment(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setEntityType(commentRequestDto.getEntityType());
        comment.setEntityId(commentRequestDto.getEntityId());
        comment.setContent(commentRequestDto.getContent());
        comment.setAuthorUsername(commentRequestDto.getAuthorUsername());
        comment.setAuthorId(commentRequestDto.getAuthorId());
        comment.setParentId(commentRequestDto.getParentId());
        comment.setMentions(commentRequestDto.getMentions());
        try{
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
            event.setParentId(saved.getParentId() != null ? String.valueOf(saved.getParentId()) : null);
            event.setMentions(saved.getMentions());
            event.setContent(saved.getContent());
            event.setAuthHeader(authHeader);

            rabbitTemplate.convertAndSend("discussion.exchange", "comment.added", event);
            return commentMapper.toCommentResponseDto(saved);
        } catch (RuntimeException e){
            log.error("Error: {}",e.getMessage());
            throw new ErrorCreatingEntry("Error creating a new comment");
        }
    }

    public CommentResponseDto getComment(String parentId) {
        try{
            ObjectId objectId = new ObjectId(parentId);
            Comment comment = commentRepository.findById(objectId)
                    .orElseThrow(RuntimeException::new);
            return commentMapper.toCommentResponseDto(comment);
        } catch (Exception e){
            log.error("Error : {}" , e.getMessage());
            throw new ResourceNotFoundException("Comment not found");
        }
    }
    public CommentResponseDto updateComment(String id, String newContent) {
        try {
            ObjectId objectId = new ObjectId(id);
            Comment comment = commentRepository.findById(objectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
            comment.setContent(newContent);
            Comment saved = commentRepository.save(comment);
            return commentMapper.toCommentResponseDto(saved);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating comment with id: {}", id);
            throw new ErrorCreatingEntry("Error updating comment");
        }
    }

    public void deleteComment(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            Comment comment = commentRepository.findById(objectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
            commentRepository.delete(comment);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting comment with id: {}", id);
            throw new ErrorCreatingEntry("Error deleting comment");
        }
    }
}

