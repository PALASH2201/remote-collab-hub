package com.remotehub.discussionservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private String id;
    private String content;
    private String authorId;
    private String entityId;
    private String entityType; // TASK, PROJECT, SPRINT
    private String parentId;
    private List<String> mentions;
    private Instant createdAt = Instant.now();
}
