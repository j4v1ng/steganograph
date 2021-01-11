package com.javing.steganograph.endpoints;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class UserInputValidationException extends RuntimeException{

    public UserInputValidationException(String message) {
        super(message);
    }
}
