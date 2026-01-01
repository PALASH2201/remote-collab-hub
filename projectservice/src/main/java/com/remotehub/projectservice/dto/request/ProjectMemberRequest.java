package com.remotehub.projectservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRequest {
    private UUID userId;
    private String userFullName;
    private String userEmail;
    private String username;
    private Object role;
}
