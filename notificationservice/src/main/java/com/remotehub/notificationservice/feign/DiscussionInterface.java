package com.remotehub.notificationservice.feign;

import com.remotehub.notificationservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "DISCUSSIONSERVICE", configuration = FeignConfig.class)
public interface DiscussionInterface {

    @GetMapping("/comments/{parentId}/username")
    ResponseEntity<String> getParentCommentAuthorUsername(@PathVariable String parentId);
}
