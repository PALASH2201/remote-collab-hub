package com.remotehub.projectservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID projectId;
    private UUID teamId;
    private String projectName;
    private String projectDesc;
    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Sprint> sprintList;
    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Task> taskList;
    private Date startDate;
    private Date endDate;
    private UUID createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}
