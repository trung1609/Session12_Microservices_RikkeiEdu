package com.trung.orderservice.exception;

import com.trung.orderservice.dto.ApiResponseError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
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

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ApiResponseError> handleInvalidDataException(InvalidDataException ex) {
        return new ResponseEntity<>(
                ApiResponseError.builder()
                        .error(Map.of("error", ex.getMessage()))
                        .message("Invalid data")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<ApiResponseError> handleServerError(ServerErrorException ex) {
        return new ResponseEntity<>(
                ApiResponseError.builder()
                        .error(Map.of("error", ex.getMessage()))
                        .message("Internal server error")
                        .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
