package com.javing.steganograph.service;

import com.javing.steganograph.web.UserInputValidationException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

/**
 * Service for validating user input for steganography operations.
 */
@Service
public class UserInputValidator {

    // Allow letters, numbers, spaces, and common punctuation
    private static final String VALID_CHARS_REGEX = "[^a-zA-Z0-9 .,!?;:'\"\\-_@#$%&*()\\[\\]{}]";

    /**
     * Validates both the image bytes and the message.
     *
     * @param bytes the image bytes
     * @param message the message to hide
     * @throws UserInputValidationException if validation fails
     */
    public void validate(final byte[] bytes, final String message) {
        validate(bytes);

        if (message == null || message.trim().isEmpty()) {
            throw new UserInputValidationException("Message cannot be empty or contain only whitespace!");
        }

        Matcher matcher = compile(VALID_CHARS_REGEX).matcher(message);
        if (matcher.find()) {
            throw new UserInputValidationException(
                "Invalid characters in message. Allowed: letters, numbers, spaces, and common punctuation (.,!?;:'\"\\-_@#$%&*()[]{})"
            );
        }
    }

    /**
     * Validates the image bytes.
     *
     * @param bytes the image bytes
     * @throws UserInputValidationException if validation fails
     */
    public void validate(final byte[] bytes) {
        if (bytes == null) {
            throw new UserInputValidationException("Image file is required but was not provided!");
        }

        if (bytes.length == 0) {
            throw new UserInputValidationException("Image file cannot be empty!");
        }
    }
}
