package com.remotehub.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InviteRequest {
    private List<String> emails;
}
