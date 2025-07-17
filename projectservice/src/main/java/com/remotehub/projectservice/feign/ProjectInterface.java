package com.remotehub.projectservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "USER-SERVICE")
public interface ProjectInterface {
    @GetMapping("teams/exists/{teamId}")
    ResponseEntity<Boolean> checkIfTeamExists(@PathVariable UUID teamId);
}
