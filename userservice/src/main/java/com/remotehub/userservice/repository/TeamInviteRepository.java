package com.remotehub.userservice.repository;

import com.remotehub.userservice.entity.TeamInvite;
import com.remotehub.userservice.enums.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamInviteRepository extends JpaRepository<TeamInvite, UUID> {
    Optional<TeamInvite> findByToken(String token);
    List<TeamInvite> findByEmailAndStatus(String email, InviteStatus status);
}

