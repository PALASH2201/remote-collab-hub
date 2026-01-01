package com.remotehub.discussionservice.exceptions;

public class ErrorDeletingEntry extends RuntimeException {
    public ErrorDeletingEntry(String message) {
        super(message);
    }
}
