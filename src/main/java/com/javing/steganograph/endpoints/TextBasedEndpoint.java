package com.javing.steganograph.endpoints;

import com.javing.steganograph.service.support.UserInputValidator;
import com.javing.steganograph.service.textbased.TextDeStegService;
import com.javing.steganograph.service.textbased.TextStegService;
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
public class TextBasedEndpoint {

    private static final String STEG_ENDPOINT = "/steg/{text}";
    private static final String DE_STEG_ENDPOINT = "/desteg";
    private final UserInputValidator userInputValidator;
    private final TextStegService textStegService;
    private final TextDeStegService textDeStegService;

    @CrossOrigin
    @RequestMapping(value = STEG_ENDPOINT, method = POST, produces = IMAGE_PNG_VALUE)
    public @ResponseBody
    ResponseEntity<Resource> steg(@PathVariable String text, @RequestParam(value = "image", required = true) final MultipartFile image) throws IOException {

        userInputValidator.validate(image.getBytes(), text);
        byte[] steg = textStegService.steg(text, image.getBytes());

        return ok()
                .contentLength(steg.length)
                .contentType(IMAGE_PNG)
                .body(new ByteArrayResource(steg));

    }

    @RequestMapping(value = DE_STEG_ENDPOINT, method = POST, produces = APPLICATION_JSON_VALUE)
    public String deSteg(@RequestPart(value = "image", required = true) final MultipartFile image) throws IOException {

        userInputValidator.validate(image.getBytes());
        return textDeStegService.deSteg(image.getBytes());
    }

}
