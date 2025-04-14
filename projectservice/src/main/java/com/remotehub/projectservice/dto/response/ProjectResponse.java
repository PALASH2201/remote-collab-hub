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
public class ProjectResponse {
    private UUID teamId;
    private String projectName;
    private String projectDesc;
    private Date startDate;
    private UUID createdBy;
}
