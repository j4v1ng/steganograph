package com.javing.steganograph.web;

import com.javing.steganograph.service.UserInputValidator;
import com.javing.steganograph.service.TextDeStegService;
import com.javing.steganograph.service.StegService;
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

@AllArgsConstructor
@RestController
public class StegEndpoint {

    private static final String TEXT_STEG_ENDPOINT = "/steg/{text}";
    private static final String TEXT_DE_STEG_ENDPOINT = "/desteg";
    private final UserInputValidator userInputValidator;
    private final StegService stegService;
    private final TextDeStegService textDeStegService;

    @CrossOrigin
    @RequestMapping(value = TEXT_STEG_ENDPOINT, method = POST, produces = IMAGE_PNG_VALUE)
    public @ResponseBody
    ResponseEntity<Resource> textSteg(@PathVariable String text, @RequestParam(value = "image", required = true) final MultipartFile image) throws IOException {

        userInputValidator.validate(image.getBytes(), text);
        byte[] steg = stegService.steg(text, image.getBytes());

        return ok()
                .contentLength(steg.length)
                .contentType(IMAGE_PNG)
                .body(new ByteArrayResource(steg));

    }

    @CrossOrigin
    @RequestMapping(value = TEXT_DE_STEG_ENDPOINT, method = POST, produces = APPLICATION_JSON_VALUE)
    public String textDeSteg(@RequestPart(value = "image", required = true) final MultipartFile image) throws IOException {

        userInputValidator.validate(image.getBytes());
        return textDeStegService.deSteg(image.getBytes());
    }

}
