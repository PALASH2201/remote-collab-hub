package com.remotehub.projectservice.entity;

import com.remotehub.projectservice.enums.Priority;
import com.remotehub.projectservice.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;
    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;
    @ManyToOne
    @JoinColumn(name="sprint_id")
    private Sprint sprint;
    private UUID assigneeId;
    private String taskTitle;
    private String taskDesc;
    @Enumerated(EnumType.STRING)
    private Status taskStatus;
    @Enumerated(EnumType.STRING)
    private Priority taskPriority;
    private Date dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
