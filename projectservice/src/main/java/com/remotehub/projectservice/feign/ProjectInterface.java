package com.remotehub.projectservice.feign;

import com.remotehub.projectservice.config.FeignConfig;
import com.remotehub.projectservice.dto.request.ProjectMemberRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "USER-SERVICE", configuration = FeignConfig.class)
public interface ProjectInterface {
    @GetMapping("teams/exists/{teamId}")
    ResponseEntity<Boolean> checkIfTeamExists(@PathVariable UUID teamId);

    @GetMapping("teams/{teamId}/members")
    ResponseEntity<List<ProjectMemberRequest>> getTeamMembers(@PathVariable UUID teamId);
}
