package com.csye6225.piggymemo.exception;

public class JoinRequestNotFoundException extends RuntimeException {
    public JoinRequestNotFoundException(String message) {
        super(message);
    }
}
