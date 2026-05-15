package com.trung.customerservice.exception;

import com.trung.customerservice.dto.ApiResponseError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

        return new ResponseEntity<>(
                ApiResponseError.builder()
                        .error(errors)
                        .message("Validation failed")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponseError.builder()
                        .error(Map.of("error", ex.getMessage()))
                        .message("Resource not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponseError> handleResourceConflictException(ResourceConflictException ex) {
        return new ResponseEntity<>(
                ApiResponseError.builder()
                        .error(Map.of("error", ex.getMessage()))
                        .message("Resource conflict")
                        .status(HttpStatus.CONFLICT.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ApiResponseError> handleInvalidCredentialException(InvalidCredentialException ex) {
        return new ResponseEntity<>(
                ApiResponseError.builder()
                        .error(Map.of("error", ex.getMessage()))
                        .message("Invalid credentials")
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }
}
