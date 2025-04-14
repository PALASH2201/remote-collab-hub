package com.remotehub.userservice.controller;

import com.remotehub.userservice.dto.response.TeamResponseDto;
import com.remotehub.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{userId}/teams")
    public ResponseEntity<List<TeamResponseDto>> getUserTeams(@PathVariable UUID userId){
        List<TeamResponseDto> list = userService.getUserTeams(userId);
        return ResponseEntity.status(200).body(list);
    }

    @GetMapping("/me/teams")
    public ResponseEntity<List<TeamResponseDto>> getTeams(){
        List<TeamResponseDto> list = userService.getTeams();
        return ResponseEntity.status(200).body(list);
    }
}
