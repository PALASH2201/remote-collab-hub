package com.remotehub.userservice.entity;

import com.remotehub.userservice.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    private String userEmail;
    private String userPassword;
    private String userFullName;
    private String userStatus;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    @OneToMany(mappedBy = "createdBy")
    private List<Teams> createdTeams;
    @OneToMany(mappedBy = "user")
    private List<TeamMemberships> teamMemberships;
}
