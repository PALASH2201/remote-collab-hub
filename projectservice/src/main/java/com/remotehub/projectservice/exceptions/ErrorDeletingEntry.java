package com.remotehub.projectservice.exceptions;

public class ErrorDeletingEntry extends RuntimeException {
    public ErrorDeletingEntry(String message) {
        super(message);
    }
}
