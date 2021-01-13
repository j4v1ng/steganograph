package com.javing.steganograph.service.support;

import com.javing.steganograph.endpoints.UserInputValidationException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@Service
public class UserInputValidator {

    public void validate(final byte[] bytes, final String message) {

        validate(bytes);
        Matcher matcher = compile("[^a-z0-9 ]", CASE_INSENSITIVE).matcher(message);

        if (matcher.find() || message.equals("") || message.equals(" ")) {
            throw new UserInputValidationException("Wrong input, only letters and numbers are accepted!");
        }

    }

    public void validate(final byte[] bytes) {

        if (bytes == null || bytes.length == 0) {
            throw new UserInputValidationException("File not present!");
        }

    }
}
