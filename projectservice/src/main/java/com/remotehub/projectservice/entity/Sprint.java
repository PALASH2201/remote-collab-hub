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
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sprintId;
    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;
    @OneToMany(mappedBy = "sprint",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> taskList;
    private UUID sprintCreator;
    private String sprintName;
    private String sprintGoal;
    private Date startDate;
    private Date endDate;
}
