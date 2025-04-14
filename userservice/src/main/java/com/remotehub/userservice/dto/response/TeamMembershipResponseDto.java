package com.remotehub.userservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamMembershipResponseDto {
    private UUID membershipId;
    private UUID userId;
    private UUID teamId;
    private String role;
    private LocalDateTime joinedAt;
}
