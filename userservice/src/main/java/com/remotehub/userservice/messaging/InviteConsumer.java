package com.remotehub.userservice.messaging;

import com.remotehub.userservice.entity.TeamInvite;
import com.remotehub.userservice.enums.InviteStatus;
import com.remotehub.userservice.repository.TeamInviteRepository;
import com.remotehub.userservice.service.InviteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
public class InviteConsumer {

    private final TeamInviteRepository inviteRepo;
    private final InviteService inviteService;

    public InviteConsumer(TeamInviteRepository inviteRepo, InviteService inviteService) {
        this.inviteRepo = inviteRepo;
        this.inviteService = inviteService;
    }

    @RabbitListener(queues = "${app.rabbitmq.invite-queue}")
    public void handleInviteMessage(String inviteIdStr) {
        log.info("Received invite message for ID: {}", inviteIdStr);
        try {
            UUID inviteId = UUID.fromString(inviteIdStr);
            TeamInvite invite = inviteRepo.findById(inviteId)
                    .orElseThrow(() -> new IllegalArgumentException("Invite not found with ID: " + inviteId));

            if (invite.getStatus() != InviteStatus.PENDING) {
                log.info("Invite {} is no longer pending. Skipping email.", inviteId);
                return;
            }
            String baseUrl = "http://localhost:5173";
            String link = baseUrl + "/invites/accept?token=" + invite.getToken();
            inviteService.sendEmail(invite.getEmail(), link);
        } catch (Exception e) {
            log.error("Error processing invite ID {}: {}", inviteIdStr, e.getMessage());
        }
    }
}
