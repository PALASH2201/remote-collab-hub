package com.remotehub.notificationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentAddedEvent {
    private String commentId;
    private String authorId;
    private String entityId;
    private String entityType;
    private String parentId;
}
