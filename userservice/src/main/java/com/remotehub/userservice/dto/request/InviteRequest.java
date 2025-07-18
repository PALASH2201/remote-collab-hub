package com.remotehub.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InviteRequest {
    List<String> emails;
}
