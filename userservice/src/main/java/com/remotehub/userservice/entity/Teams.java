package com.remotehub.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID teamId;
    private String teamName;
    private String teamDesc;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    @OneToMany(mappedBy = "teams")
    private List<TeamMemberships> teamMemberships;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
