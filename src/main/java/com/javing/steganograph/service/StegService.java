package com.javing.steganograph.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static javax.imageio.ImageIO.write;

@AllArgsConstructor
@Service
public class StegService {

    private final ImageProcessingService imageProcessingService;

    public byte[] steg(final String message, final byte[] imageInput) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        write(imageProcessingService.processImage(message, imageInput), "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
