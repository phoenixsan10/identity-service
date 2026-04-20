package com.phoenixsan.identity.identity_service.exception;

public class CustomAccessDeniedException extends RuntimeException {

    public CustomAccessDeniedException(String message) {
        super(message);
    }
}