package com.remotehub.discussionservice.exceptions;


public class ErrorCreatingEntry extends RuntimeException {
    public ErrorCreatingEntry(String message) {
        super(message);
    }
}