package com.example.ecommerce.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String errorMessage = ex.getMessage();
        HttpStatus status = switch (errorMessage) {
            case "User not found", "Product not found" -> HttpStatus.NOT_FOUND;
            case "Invalid credentials" -> HttpStatus.UNAUTHORIZED;
            case "User already exists!" -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        return ResponseEntity.status(status).body(Map.of("error", errorMessage));
    }
}
