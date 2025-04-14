package com.remotehub.projectservice.dto.response;

import com.remotehub.projectservice.entity.Sprint;
import com.remotehub.projectservice.entity.Task;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private UUID projectId;
    private UUID teamId;
    private String projectName;
    private String projectDesc;
    private Date startDate;
    private Date endDate;
    private List<SprintResponse> sprintList;
    private List<TaskResponse> taskList;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private UUID createdBy;
}
