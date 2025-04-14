package com.remotehub.userservice.entity;

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
public class TeamMemberships {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID membershipId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Teams teams;
    private String role;
    private LocalDateTime joinedAt;
}
