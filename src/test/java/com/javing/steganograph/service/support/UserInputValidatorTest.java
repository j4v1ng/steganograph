package com.javing.steganograph.service.support;

import com.javing.steganograph.endpoints.UserInputValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UserInputValidatorTest {

    private UserInputValidator userInputValidator = new UserInputValidator();

    @ParameterizedTest
    @ValueSource(strings = {"", "<>", "!@", " "})
    void shouldFailForWrongMessageInput(String arg) {
        byte[] someBytes = {1};

        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(someBytes, arg);
        }, "Wrong input, only letters and numbers are accepted!");
    }

    @Test
    void shouldFailValidationForMissingFile() {

        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(null, "some message");
        }, "File not present!");
    }

    @Test
    void shouldFailValidationForEmptyFile() {

        byte[] someBytes = {};

        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(someBytes, "some message");
        }, "File not present!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "AbC", "123", "a1", "A1"})
    void shouldPassValidation(String arg) {

        assertDoesNotThrow(() -> {
            userInputValidator.validate(new byte[]{1}, arg);
        });
    }
}