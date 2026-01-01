package com.remotehub.discussionservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
