package com.remotehub.userservice.dto.response;

import com.remotehub.userservice.enums.Role;
import lombok.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private UUID userId;
    private String userFullName;
    private String userEmail;
    private String username;
    private Role role;
}
