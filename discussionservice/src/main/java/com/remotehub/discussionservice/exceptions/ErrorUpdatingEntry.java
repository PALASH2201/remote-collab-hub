package com.remotehub.discussionservice.exceptions;

public class ErrorUpdatingEntry extends RuntimeException {
    public ErrorUpdatingEntry(String message) {
        super(message);
    }
}
