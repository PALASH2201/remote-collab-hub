package com.remotehub.projectservice.dto.response;

import com.remotehub.projectservice.enums.Priority;
import com.remotehub.projectservice.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private UUID taskId;
    private UUID projectId;
    private UUID sprintId;
    private UUID assigneeId;
    private String taskTitle;
    private String taskDesc;
    private Status taskStatus;
    private Priority taskPriority;
    private Date dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
