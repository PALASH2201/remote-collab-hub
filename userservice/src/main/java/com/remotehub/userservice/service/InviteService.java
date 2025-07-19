package com.remotehub.userservice.service;

import com.remotehub.userservice.entity.TeamInvite;
import com.remotehub.userservice.entity.User;
import com.remotehub.userservice.enums.InviteStatus;
import com.remotehub.userservice.exceptions.ExpiredInviteError;
import com.remotehub.userservice.exceptions.InvalidTokenException;
import com.remotehub.userservice.messaging.InviteProducer;
import com.remotehub.userservice.repository.TeamInviteRepository;
import com.remotehub.userservice.repository.UserRepository;
import com.remotehub.userservice.utils.InviteTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class InviteService {

    private final TeamInviteRepository inviteRepo;
    private final InviteTokenUtil tokenUtil;
    private final JavaMailSender mailSender;
    private final TeamService teamService;
    private final UserRepository userRepository;
    private final InviteProducer inviteProducer;

    public InviteService(TeamInviteRepository inviteRepo, InviteTokenUtil tokenUtil, JavaMailSender mailSender, TeamService teamService, UserRepository userRepository, InviteProducer inviteProducer) {
        this.inviteRepo = inviteRepo;
        this.tokenUtil = tokenUtil;
        this.mailSender = mailSender;
        this.teamService = teamService;
        this.userRepository = userRepository;
        this.inviteProducer = inviteProducer;
    }

    public ResponseEntity<String> sendInvites(UUID teamId, List<String> emails) {
        try{
            List<TeamInvite> invitesToCreate = new ArrayList<>();
            for (String email : emails) {
                String token = tokenUtil.generateToken(email, teamId);
                TeamInvite invite = new TeamInvite();
                invite.setEmail(email);
                invite.setTeamId(teamId);
                invite.setToken(token);
                invite.setStatus(InviteStatus.PENDING);
                invite.setCreatedAt(LocalDateTime.now());
                invite.setExpiresAt(LocalDateTime.now().plusDays(1));
                invitesToCreate.add(invite);
            }
            List<TeamInvite> savedInvites = inviteRepo.saveAll(invitesToCreate);
            for (TeamInvite invite : savedInvites) {
                inviteProducer.sendInviteMessage(invite.getId());
            }
            return new ResponseEntity<>("Invites are being processed and will be sent shortly.", HttpStatus.ACCEPTED);
        } catch(Exception e){
            return new ResponseEntity<>("Error sending invites",HttpStatus.BAD_REQUEST);
        }
    }

    public void sendEmail(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("You're invited to join a team!");
        message.setText("Click the link to accept: " + link);
        mailSender.send(message);
    }

    @Transactional
    public ResponseEntity<String> acceptInvite(String token) {
        try{
            Claims claims = tokenUtil.parseToken(token);

            String email = claims.getSubject();
            UUID teamId = UUID.fromString((String) claims.get("teamId"));

            TeamInvite invite = inviteRepo.findByToken(token)
                    .orElseThrow(() -> new InvalidTokenException("Invalid token"));

            if (invite.getStatus() != InviteStatus.PENDING) {
                throw new ExpiredInviteError("Invite already used or expired");
            }

            if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
                invite.setStatus(InviteStatus.EXPIRED);
                inviteRepo.save(invite);
                throw new ExpiredInviteError("Invite has expired");
            }
            log.info("Add {} to team {}", email, teamId);
            User user = userRepository.findByUserEmail(email);
            if(user == null) throw new UsernameNotFoundException("Cannot find user with email: "+email);
            teamService.addNewMember(teamId,user.getUserId());
            invite.setStatus(InviteStatus.ACCEPTED);
            inviteRepo.save(invite);
            return new ResponseEntity<>("Invite accepted successfully!", HttpStatus.OK);
        } catch(RuntimeException e){
            return new ResponseEntity<>("Error in accepting team invite",HttpStatus.BAD_REQUEST);
        }
    }
}

