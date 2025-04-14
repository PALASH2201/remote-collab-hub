package com.remotehub.userservice.exceptions;

public class ErrorDeletingEntry extends RuntimeException {
    public ErrorDeletingEntry(String message) {
        super(message);
    }
}
