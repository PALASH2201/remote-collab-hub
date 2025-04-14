package com.remotehub.projectservice.dto.request;

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
public class ProjectRequest {
    private UUID teamId;
    private String projectName;
    private String projectDesc;
    private Date startDate;
    private UUID createdBy;
}
