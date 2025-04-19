package com.javing.steganograph.web;

import com.javing.steganograph.service.SteganographyService;
import com.javing.steganograph.service.UserInputValidator;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * REST controller for steganography operations.
 */
@AllArgsConstructor
@RestController
public class StegEndpoint {

    private static final String ENCODE_ENDPOINT = "/steg/{text}";
    private static final String DECODE_ENDPOINT = "/desteg";

    private final UserInputValidator userInputValidator;
    private final SteganographyService steganographyService;

    /**
     * Encodes a message into an image using steganography.
     *
     * @param text the text to hide
     * @param image the image file
     * @return the processed image with hidden text
     * @throws IOException if there's an error processing the image
     */
    @CrossOrigin
    @RequestMapping(value = ENCODE_ENDPOINT, method = POST, produces = IMAGE_PNG_VALUE)
    public @ResponseBody ResponseEntity<Resource> encodeMessage(
            @PathVariable String text, 
            @RequestParam(value = "image", required = true) final MultipartFile image) throws IOException {

        byte[] imageBytes = image.getBytes();
        userInputValidator.validate(imageBytes, text);
        byte[] processedImage = steganographyService.encodeMessage(text, imageBytes);

        return ok()
                .contentLength(processedImage.length)
                .contentType(IMAGE_PNG)
                .body(new ByteArrayResource(processedImage));
    }

    /**
     * Extracts a hidden message from an image.
     *
     * @param image the image file
     * @return the extracted text
     * @throws IOException if there's an error processing the image
     */
    @CrossOrigin
    @RequestMapping(value = DECODE_ENDPOINT, method = POST, produces = APPLICATION_JSON_VALUE)
    public String decodeMessage(@RequestPart(value = "image", required = true) final MultipartFile image) throws IOException {
        byte[] imageBytes = image.getBytes();
        userInputValidator.validate(imageBytes);
        return steganographyService.decodeMessage(imageBytes);
    }
}
