package com.javing.steganograph.service.support;

import com.javing.steganograph.endpoints.UserInputValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserInputValidatorTest {

    private UserInputValidator userInputValidator = new UserInputValidator();

    @ParameterizedTest
    @ValueSource(strings = {"", "<>", "!@", " "})
    void shouldFailForWrongMessageInput(String arg) {
        byte[] someBytes = {1};

        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(someBytes, arg);
        });
    }

    @Test
    void shouldFailValidationForMissingFile() {

        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(null, "some message");
        });
    }

    @Test
    void shouldFailValidationForEmptyFile() {

        byte[] someBytes = {};

        assertThrows(UserInputValidationException.class, () -> {
            userInputValidator.validate(someBytes, "some message");
        });
    }

}