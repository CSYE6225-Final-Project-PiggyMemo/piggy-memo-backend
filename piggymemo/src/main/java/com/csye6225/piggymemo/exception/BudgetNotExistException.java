package com.csye6225.piggymemo.exception;

public class BudgetNotExistException extends RuntimeException {
    public BudgetNotExistException(String message) {
        super(message);
    }
}
