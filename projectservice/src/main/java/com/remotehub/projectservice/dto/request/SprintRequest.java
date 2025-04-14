package com.remotehub.projectservice.dto.request;

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
public class SprintRequest {
    private UUID projectId;
    private UUID sprintCreator;
    private String sprintName;
    private String sprintGoal;
    private Date startDate;
    private Date endDate;
}
