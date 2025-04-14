package com.remotehub.userservice.exceptions;

public class ErrorUpdatingEntry extends RuntimeException {
    public ErrorUpdatingEntry(String message) {
        super(message);
    }
}
