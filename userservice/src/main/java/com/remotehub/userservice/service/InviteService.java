package com.remotehub.userservice.service;

import com.remotehub.userservice.entity.TeamInvite;
import com.remotehub.userservice.enums.InviteStatus;
import com.remotehub.userservice.exceptions.ExpiredInviteError;
import com.remotehub.userservice.repository.TeamInviteRepository;
import com.remotehub.userservice.utils.InviteTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteService {

    private final TeamInviteRepository inviteRepo;
    private final InviteTokenUtil tokenUtil;
    private final JavaMailSender mailSender;
    private final TeamService teamService;

    public void sendInvites(UUID teamId, List<String> emails) {
        for (String email : emails) {
            String token = tokenUtil.generateToken(email, teamId);
            TeamInvite invite = new TeamInvite();
            invite.setEmail(email);
            invite.setTeamId(teamId);
            invite.setToken(token);
            invite.setStatus(InviteStatus.PENDING);
            invite.setCreatedAt(LocalDateTime.now());
            invite.setExpiresAt(LocalDateTime.now().plusDays(1));

            inviteRepo.save(invite);

            String link = "http://localhost:5173/invite/accept?token=" + token;
            sendEmail(email, link);
        }
    }

    private void sendEmail(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("You're invited to join a team!");
        message.setText("Click the link to accept: " + link);
        mailSender.send(message);
    }

    @Transactional
    public void acceptInvite(String token) {
        Claims claims = tokenUtil.parseToken(token);

        String email = claims.getSubject();
        UUID teamId = UUID.fromString((String) claims.get("teamId"));

        TeamInvite invite = inviteRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (invite.getStatus() != InviteStatus.PENDING) {
            throw new ExpiredInviteError("Invite already used or expired");
        }

        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            invite.setStatus(InviteStatus.EXPIRED);
            inviteRepo.save(invite);
            throw new ExpiredInviteError("Invite has expired");
        }

        // Add user to team (you'll need to fetch user by email via user-service)
        // For now, just log it
        log.info("Add {} to team {}", email, teamId);
        invite.setStatus(InviteStatus.ACCEPTED);
        inviteRepo.save(invite);
    }
}

