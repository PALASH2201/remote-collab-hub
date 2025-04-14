package com.remotehub.userservice.dto.response;

import com.remotehub.userservice.enums.Role;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private UUID userId;
    private String userEmail;
    private String userPassword;
    private String userFullName;
    private String userStatus;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    private List<TeamResponseDto> createdTeams;
    private List<TeamMembershipResponseDto> teamMemberships;
}
