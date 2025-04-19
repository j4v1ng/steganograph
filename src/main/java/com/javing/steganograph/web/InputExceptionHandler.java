package com.javing.steganograph.web;

import com.javing.steganograph.exception.ImageCapacityException;
import com.javing.steganograph.exception.SteganographyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class InputExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserInputValidationException.class, ImageCapacityException.class})
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {SteganographyException.class})
    protected ResponseEntity<Object> handleSteganographyException(SteganographyException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "An unexpected error occurred: " + ex.getMessage(), 
                new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }
}
