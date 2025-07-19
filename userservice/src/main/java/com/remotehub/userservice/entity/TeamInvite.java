package com.remotehub.userservice.entity;

import com.remotehub.userservice.enums.InviteStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private UUID teamId;
    private String token;
    @Enumerated(EnumType.STRING)
    private InviteStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
