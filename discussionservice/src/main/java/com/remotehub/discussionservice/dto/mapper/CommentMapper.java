package com.remotehub.discussionservice.dto.mapper;

import com.remotehub.discussionservice.dto.response.CommentResponseDto;
import com.remotehub.discussionservice.entity.Comment;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" , builder = @Builder(disableBuilder = true))
public interface CommentMapper {
    CommentResponseDto toCommentResponseDto(Comment comment);
}
