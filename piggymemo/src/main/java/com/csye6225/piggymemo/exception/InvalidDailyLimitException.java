package com.csye6225.piggymemo.exception;

public class InvalidDailyLimitException extends RuntimeException {
    public InvalidDailyLimitException(String message) {
        super(message);
    }
}
