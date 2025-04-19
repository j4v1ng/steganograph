package com.javing.steganograph.service;

import com.javing.steganograph.exception.ImageCapacityException;
import com.javing.steganograph.exception.SteganographyException;
import com.javing.steganograph.util.BitManipulationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

/**
 * Service for processing images for steganography operations.
 */
@AllArgsConstructor
@Service
public class ImageProcessingService {

    private static final String SEPARATOR = ":";
    private final PointerService pointerService;

    /**
     * Processes an image to hide a payload using steganography.
     *
     * @param payload the text to hide
     * @param imageInput the image bytes
     * @return the processed image with hidden text
     * @throws IOException if there's an error reading the image
     * @throws ImageCapacityException if the image is too small to store the payload
     * @throws SteganographyException if there's an error during steganography
     */
    public BufferedImage processImage(String payload, byte[] imageInput) throws IOException {
        try {
            BufferedImage image = read(new ByteArrayInputStream(imageInput));

            Point point = new Point(0, 0);
            String fullPayload = payload.length() + SEPARATOR + payload;
            byte[] messageBytes = fullPayload.getBytes();

            // Check if the image has enough capacity
            if (fullPayload.length() * 8 > (image.getWidth() * image.getHeight())) {
                throw new ImageCapacityException(
                    "The image is too small to store the payload! Required: " + 
                    (fullPayload.length() * 8) + " pixels, Available: " + 
                    (image.getWidth() * image.getHeight()) + " pixels"
                );
            }

            // Hide each bit of the message in the least significant bit of each pixel
            for (int currentByte : messageBytes) {
                for (int index = 0; index < 8; index++) {
                    boolean bitValue = (currentByte & 128) == 128;
                    image.setRGB(
                        point.x, 
                        point.y, 
                        BitManipulationUtil.setLeastSignificantBit(image.getRGB(point.x, point.y), bitValue)
                    );
                    currentByte = currentByte << 1;
                    pointerService.nextPixel(point, image);
                }
            }

            return image;
        } catch (IOException e) {
            throw new SteganographyException("Error reading image: " + e.getMessage(), e);
        }
    }
}
