package com.remotehub.discussionservice.controller;

import com.remotehub.discussionservice.entity.Comment;
import com.remotehub.discussionservice.repository.CommentRepository;
import com.remotehub.discussionservice.service.CommentService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    public CommentController(CommentService commentService, CommentRepository commentRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public List<Comment> getComments(@PathVariable String entityType, @PathVariable String entityId) {
        return commentRepository.findByEntityIdAndEntityType(entityId, entityType);
    }

    @GetMapping("/thread/{parentId}")
    public List<Comment> getThread(@PathVariable String parentId) {
        return commentRepository.findByParentId(parentId);
    }

    @GetMapping("/{parentId}/username")
    public ResponseEntity<String> getParentCommentAuthorUsername(@PathVariable String parentId){
        Comment comment = commentService.getComment(parentId);
        return ResponseEntity.status(200).body(comment.getAuthorUsername());
    }
}

