package com.remotehub.userservice.controller;

import com.remotehub.userservice.dto.request.InviteRequest;
import com.remotehub.userservice.service.InviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/teams")
public class TeamInviteController {

    private final InviteService inviteService;

    public TeamInviteController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @PostMapping("/{teamId}/invite")
    public ResponseEntity<String> inviteMembers(@PathVariable UUID teamId, @RequestBody InviteRequest request) {
        return inviteService.sendInvites(teamId, request.getEmails());
    }

    @GetMapping("/invites/accept")
    public ResponseEntity<String> acceptInvite(@RequestParam String token) {
        return inviteService.acceptInvite(token);
    }

}
