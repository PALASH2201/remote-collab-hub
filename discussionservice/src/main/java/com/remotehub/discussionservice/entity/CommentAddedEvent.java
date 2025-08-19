package com.remotehub.discussionservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentAddedEvent {
    private String commentId;
    private String authorId;
    private String entityId;
    private String entityType;
    private String parentId;
}
