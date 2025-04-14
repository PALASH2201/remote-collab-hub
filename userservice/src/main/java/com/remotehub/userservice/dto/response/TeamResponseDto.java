package com.remotehub.userservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponseDto {
    private UUID teamId;
    private String teamName;
    private String teamDesc;
    private UUID createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TeamMembershipResponseDto> teamMemberships;
}
