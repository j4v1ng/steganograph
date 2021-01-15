package com.javing.steganograph.endpoints;


import com.javing.steganograph.service.support.UserInputValidator;
import com.javing.steganograph.service.textbased.TextDeStegService;
import com.javing.steganograph.service.textbased.TextStegService;
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

@ExtendWith(MockitoExtension.class)
class SteganographyEndpointTest {

    @Mock
    private UserInputValidator userInputValidator;
    @Mock
    private TextStegService textStegService;
    @Mock
    private TextDeStegService textDeStegService;
    @InjectMocks
    private SteganographyEndpoint steganographyEndpoint;

    @Test
    void shouldReplyWith200ForCorrectValidationDuringSteg() throws IOException {

        String message = "some text";
        byte[] imageInput = new byte[1];
        doNothing().when(userInputValidator).validate(imageInput, message);

        when(textStegService.steg(message, imageInput)).thenReturn(imageInput);
        ResponseEntity<Resource> response = steganographyEndpoint.textSteg(message, new MockMultipartFile("filename.png", imageInput));

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void shouldReplyWith200ForCorrectValidationDuringDeSteg() throws IOException {

        String message = "some text";
        byte[] imageInput = new byte[1];
        doNothing().when(userInputValidator).validate(imageInput);

        when(textDeStegService.deSteg(imageInput)).thenReturn(message);
        String response = steganographyEndpoint.textDeSteg(new MockMultipartFile("filename.png", imageInput));

        assertThat(response).isEqualTo(message);
    }


}