package com.remotehub.notificationservice.entity;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentAddedEvent {
    private String commentId;
    private UUID authorId;
    private String authorUsername;
    private UUID entityId;
    private String entityType;
    private String parentId;
    private String content;
    private String authHeader;
    private List<String> mentions;
}
