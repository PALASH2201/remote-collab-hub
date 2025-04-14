package com.remotehub.userservice.dto.response;

import com.remotehub.userservice.enums.Role;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private UUID userId;
    private String userEmail;
    private String userFullName;
    private String userStatus;
    private Role role;
    private List<TeamMembershipResponseDto> teamMemberships;
}
