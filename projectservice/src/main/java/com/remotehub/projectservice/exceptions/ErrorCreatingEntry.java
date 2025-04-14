package com.remotehub.projectservice.exceptions;


public class ErrorCreatingEntry extends RuntimeException {
    public ErrorCreatingEntry(String message) {
        super(message);
    }
}