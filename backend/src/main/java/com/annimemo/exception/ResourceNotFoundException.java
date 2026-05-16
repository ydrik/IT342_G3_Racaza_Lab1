package com.annimemo.exception;

/**
 * Resource Not Found Exception
 * Custom exception for when a requested resource is not found
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
