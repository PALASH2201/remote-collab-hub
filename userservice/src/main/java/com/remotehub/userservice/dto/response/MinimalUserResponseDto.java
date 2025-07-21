package com.remotehub.userservice.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MinimalUserResponseDto {
    private UUID userId;
    private String userFullName;
    private String userEmail;
}
