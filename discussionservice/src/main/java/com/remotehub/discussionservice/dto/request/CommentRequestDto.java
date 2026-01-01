package com.remotehub.discussionservice.dto.request;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentRequestDto {
    private String content;
    private UUID authorId;
    private String authorUsername;
    private UUID entityId;
    private String entityType; // TASK, PROJECT, SPRINT
    private ObjectId parentId;
    private List<String>mentions;
}
