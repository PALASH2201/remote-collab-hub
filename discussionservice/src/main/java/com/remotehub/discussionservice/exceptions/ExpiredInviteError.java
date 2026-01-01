package com.remotehub.discussionservice.exceptions;

public class ExpiredInviteError extends RuntimeException {
    public ExpiredInviteError(String message) {
        super(message);
    }
}
