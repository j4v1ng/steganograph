package com.javing.steganograph.service.support;

import com.javing.steganograph.endpoints.UserInputValidationException;
import org.springframework.stereotype.Service;

@Service
public class UserInputValidator {

    public void validate(final byte[] bytes, final String message) {
        validate(bytes);
        if (message == null || message.equals("")) {
            throw new UserInputValidationException("Wrong input!");
        }
    }

    public void validate(final byte[] bytes) {
        if (bytes.length == 0) {
            throw new UserInputValidationException("Wrong input!");
        }
    }
}
