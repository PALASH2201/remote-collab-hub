package com.remotehub.discussionservice.repository;

import com.remotehub.discussionservice.entity.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {
    List<Comment> findByEntityIdAndEntityType(String entityId, String entityType);
    List<Comment> findByParentId(String parentId);
}
