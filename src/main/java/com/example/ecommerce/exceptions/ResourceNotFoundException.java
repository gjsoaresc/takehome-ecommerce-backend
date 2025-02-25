package com.example.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {
    public ResourceNotFoundException(String resourceName, String field, Object value) {
        super(HttpStatus.NOT_FOUND, resourceName + " not found with " + field + " = " + value);
    }
}
