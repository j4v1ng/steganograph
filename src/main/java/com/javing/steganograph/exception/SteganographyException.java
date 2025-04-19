package com.javing.steganograph.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an error occurs during steganography operations.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SteganographyException extends RuntimeException {

    /**
     * Constructs a new SteganographyException with the specified detail message.
     *
     * @param message the detail message
     */
    public SteganographyException(String message) {
        super(message);
    }

    /**
     * Constructs a new SteganographyException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public SteganographyException(String message, Throwable cause) {
        super(message, cause);
    }
}