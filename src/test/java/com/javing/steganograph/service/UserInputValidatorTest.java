package com.javing.steganograph.service;

import com.javing.steganograph.web.UserInputValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the UserInputValidator.
 */
class UserInputValidatorTest {

    private UserInputValidator userInputValidator = new UserInputValidator();

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void shouldFailForEmptyOrWhitespaceMessage(String arg) {
        byte[] someBytes = {1};

        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(someBytes, arg);
        }, "Message cannot be empty or contain only whitespace!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"<script>", "€£¥", "\\", "~`|"})
    void shouldFailForInvalidCharacters(String arg) {
        byte[] someBytes = {1};

        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(someBytes, arg);
        });
    }

    @Test
    void shouldFailValidationForMissingFile() {
        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(null, "some message");
        }, "Image file is required but was not provided!");
    }

    @Test
    void shouldFailValidationForEmptyFile() {
        byte[] someBytes = {};

        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(someBytes, "some message");
        }, "Image file cannot be empty!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "AbC", "123", "a1", "A1", "Hello, World!", "Test: 123", "a@b.com", "Name (Nickname)"})
    void shouldPassValidation(String arg) {
        assertDoesNotThrow(() -> {
            userInputValidator.validate(new byte[]{1}, arg);
        });
    }
}
