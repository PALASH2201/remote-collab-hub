package com.remotehub.discussionservice.controller;

import com.remotehub.discussionservice.entity.Comment;
import com.remotehub.discussionservice.repository.CommentRepository;
import com.remotehub.discussionservice.service.CommentService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}

