package com.remotehub.notificationservice.feign;

import com.remotehub.notificationservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "PROJECT-SERVICE", configuration = FeignConfig.class)
public interface ProjectInterface {
    @GetMapping("/project/{projectId}/members")
    ResponseEntity<List<String>> getProjectMembers(@PathVariable UUID projectId);
}
