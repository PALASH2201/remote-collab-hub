package com.remotehub.discussionservice.dto.response;

import lombok.*;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentResponseDto {
    private ObjectId id;
    private String content;
    private UUID authorId;
    private String authorUsername;
    private UUID entityId;
    private String entityType; // TASK, PROJECT, SPRINT
    private ObjectId parentId;
    private List<String> mentions;
    private Instant createdAt;
}