package com.access.exceptions;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
