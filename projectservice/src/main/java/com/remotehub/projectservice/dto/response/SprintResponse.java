package com.remotehub.projectservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SprintResponse {
    private UUID projectId;
    private UUID sprintCreator;
    private String sprintName;
    private String sprintGoal;
    private Date startDate;
    private Date endDate;
}
