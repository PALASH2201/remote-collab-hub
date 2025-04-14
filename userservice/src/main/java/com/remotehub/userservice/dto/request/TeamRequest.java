package com.remotehub.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TeamRequest {
    private String teamName;
    private String teamDesc;
    private UUID createdBy;
}
