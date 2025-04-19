package com.javing.steganograph.service;

import com.javing.steganograph.exception.SteganographyException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Consolidated service for steganography operations.
 * Handles both encoding text into images and extracting text from images.
 */
@AllArgsConstructor
@Service
public class SteganographyService {

    private final ImageProcessingService imageProcessingService;
    private final TextDeStegService textDeStegService;

    /**
     * Encodes a message into an image using steganography.
     *
     * @param message the text to hide
     * @param imageInput the image bytes
     * @return the processed image with hidden text as a byte array
     * @throws SteganographyException if there's an error during steganography
     */
    public byte[] encodeMessage(final String message, final byte[] imageInput) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedImage processedImage = imageProcessingService.processImage(message, imageInput);
            ImageIO.write(processedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SteganographyException("Error encoding message: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts a hidden message from an image.
     *
     * @param imageInput the image bytes
     * @return the extracted text
     * @throws SteganographyException if there's an error during steganography
     */
    public String decodeMessage(final byte[] imageInput) {
        try {
            return textDeStegService.deSteg(imageInput);
        } catch (IOException e) {
            throw new SteganographyException("Error decoding message: " + e.getMessage(), e);
        }
    }
}