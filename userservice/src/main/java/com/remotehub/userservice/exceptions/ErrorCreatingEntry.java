package com.remotehub.userservice.exceptions;


public class ErrorCreatingEntry extends RuntimeException {
    public ErrorCreatingEntry(String message) {
        super(message);
    }
}