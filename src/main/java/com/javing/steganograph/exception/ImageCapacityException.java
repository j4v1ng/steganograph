package com.javing.steganograph.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an image does not have enough capacity to store the payload.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageCapacityException extends RuntimeException {

    /**
     * Constructs a new ImageCapacityException with the specified detail message.
     *
     * @param message the detail message
     */
    public ImageCapacityException(String message) {
        super(message);
    }
}