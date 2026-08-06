package com.csye6225.piggymemo.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.UnexpectedRollbackException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicatedUsername(UsernameAlreadyExistsException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException e) {
        String message = e
            .getBindingResult()
            .getFieldErrors()
            .stream().map((err) -> err.getDefaultMessage()).collect(Collectors.joining("; "))
        ;
        
        return Map.of("message", message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleBadCredentials(BadCredentialsException e) {
        String message = e.getMessage();
        
        return Map.of("message", message);
    }

    @ExceptionHandler(InvalidDailyLimitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidDailyLimit(InvalidDailyLimitException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(FamilyBudgetAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleFamilyBudgetAccessDenied(FamilyBudgetAccessDeniedException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(BudgetNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleBudgetNotExist(BudgetNotExistException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnexpecterRollback(UnexpectedRollbackException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(FamilyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFamilyNotFound(FamilyNotFoundException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(InvalidFamilyCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleInvalidFamilyCode(InvalidFamilyCodeException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(AlreadyInFamilyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyInFamily(AlreadyInFamilyException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(NotInFamilyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotInFamily(NotInFamilyException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(JoinRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleJoinRequestNotFound(JoinRequestNotFoundException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(FamilyPermissionDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleFamilyPermissionDenied(FamilyPermissionDeniedException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(FamilyOwnerCannotLeaveException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleFamilyOwnerCannotLeave(FamilyOwnerCannotLeaveException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(FamilyMemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFamilyMemberNotFound(FamilyMemberNotFoundException e) {
        return Map.of("message", e.getMessage());
    }
}
