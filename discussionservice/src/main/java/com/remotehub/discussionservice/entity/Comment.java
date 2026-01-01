package com.remotehub.discussionservice.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private ObjectId id;
    private String content;
    private UUID authorId;
    private String authorUsername;
    private UUID entityId;
    private String entityType; // TASK, PROJECT, SPRINT
    private ObjectId parentId;
    private List<String> mentions;
    private Instant createdAt = Instant.now();
}
