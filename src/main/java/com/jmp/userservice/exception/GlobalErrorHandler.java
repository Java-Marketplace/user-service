package com.jmp.userservice.exception;

import com.jmp.userservice.dto.exception.GlobalErrorResponse;
import com.jmp.userservice.exception.model.EmailAlreadyUseException;
import com.jmp.userservice.exception.model.PhoneAlreadyUseException;
import com.jmp.userservice.exception.model.UserNotFoundById;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(EmailAlreadyUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GlobalErrorResponse handleEmailAlreadyUseException(EmailAlreadyUseException e) {
        return createGlobalErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(PhoneAlreadyUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GlobalErrorResponse handlePhoneAlreadyUseException(PhoneAlreadyUseException e) {
        return createGlobalErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundById.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GlobalErrorResponse handleUserNotFoundById(UserNotFoundById e) {
        return createGlobalErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    private GlobalErrorResponse createGlobalErrorResponse(HttpStatus status, String message) {
        return new GlobalErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                status.value(),
                status.getReasonPhrase(),
                message
        );
    }
}
