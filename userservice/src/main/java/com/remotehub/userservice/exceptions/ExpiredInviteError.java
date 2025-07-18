package com.remotehub.userservice.exceptions;

public class ExpiredInviteError extends RuntimeException {
    public ExpiredInviteError(String message) {
        super(message);
    }
}
