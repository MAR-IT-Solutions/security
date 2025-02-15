package com.mar.solutions.security.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(Exception e) {
        super(e.getMessage());
    }
}
