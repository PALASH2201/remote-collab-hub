package com.remotehub.userservice.controller;

import com.remotehub.userservice.dto.request.InviteRequest;
import com.remotehub.userservice.service.InviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamInviteController {

    private final InviteService inviteService;

    @PostMapping("/{teamId}/invite")
    public ResponseEntity<String> inviteMembers(@PathVariable UUID teamId, @RequestBody InviteRequest request) {
        inviteService.sendInvites(teamId, request.getEmails());
        return ResponseEntity.ok("Invites sent successfully.");
    }

    @GetMapping("/invites/accept")
    public ResponseEntity<String> acceptInvite(@RequestParam String token) {
        inviteService.acceptInvite(token);
        return ResponseEntity.ok("Invite accepted successfully!");
    }

}
