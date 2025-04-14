package com.remotehub.projectservice.dto.request;

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
public class TaskRequest {
    private UUID assigneeId;
    private String taskTitle;
    private String taskDesc;
    private Status taskStatus;
    private Priority taskPriority;
    private Date dueDate;
}
