package com.javing.steganograph.web;

import com.javing.steganograph.service.SteganographyService;
import com.javing.steganograph.service.UserInputValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

/**
 * Tests for the StegEndpoint controller.
 */
@ExtendWith(MockitoExtension.class)
class StegEndpointTest {

    @Mock
    private UserInputValidator userInputValidator;

    @Mock
    private SteganographyService steganographyService;

    @InjectMocks
    private StegEndpoint stegEndpoint;

    @Test
    void shouldReplyWith200ForCorrectValidationDuringEncoding() throws IOException {
        // Arrange
        String message = "some text";
        byte[] imageInput = new byte[1];
        MockMultipartFile imageFile = new MockMultipartFile("filename.png", imageInput);

        doNothing().when(userInputValidator).validate(imageInput, message);
        when(steganographyService.encodeMessage(message, imageInput)).thenReturn(imageInput);

        // Act
        ResponseEntity<Resource> response = stegEndpoint.encodeMessage(message, imageFile);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldReplyWith200ForCorrectValidationDuringDecoding() throws IOException {
        // Arrange
        String message = "some text";
        byte[] imageInput = new byte[1];
        MockMultipartFile imageFile = new MockMultipartFile("filename.png", imageInput);

        doNothing().when(userInputValidator).validate(imageInput);
        when(steganographyService.decodeMessage(imageInput)).thenReturn(message);

        // Act
        String response = stegEndpoint.decodeMessage(imageFile);

        // Assert
        assertThat(response).isEqualTo(message);
    }
}
