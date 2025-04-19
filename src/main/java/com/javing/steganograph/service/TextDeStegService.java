package com.javing.steganograph.service;

import com.javing.steganograph.exception.SteganographyException;
import com.javing.steganograph.util.BitManipulationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.lang.Integer.parseInt;
import static javax.imageio.ImageIO.read;

/**
 * Service for extracting hidden text from images.
 */
@AllArgsConstructor
@Service
public class TextDeStegService {

    private static final char SEPARATOR = ':';
    private final PointerService pointerService;

    /**
     * Extracts hidden text from an image.
     *
     * @param fileBytes the image bytes
     * @return the extracted text
     * @throws IOException if there's an error reading the image
     * @throws SteganographyException if there's an error during steganography
     */
    public String deSteg(byte[] fileBytes) throws IOException {
        try {
            BufferedImage image = read(new ByteArrayInputStream(fileBytes));

            StringBuilder output = new StringBuilder();
            int bitsInByte = 1;
            int extractedValue = 0;
            boolean gotLength = false;
            int messageLength = 0;
            int currentMessageLength = 0;
            Point point = new Point(0, 0);

            while (!gotLength || currentMessageLength < messageLength) {
                // Extract the least significant bit from the current pixel
                if (BitManipulationUtil.isLeastSignificantBitSet(image.getRGB(point.x, point.y))) {
                    extractedValue += 1;
                }

                // If we've collected 8 bits (a complete byte)
                if (bitsInByte == 8) {
                    // Check if we've reached the separator between length and message
                    if (!gotLength && SEPARATOR == extractedValue) {
                        try {
                            gotLength = true;
                            messageLength = parseInt(output.toString());
                            currentMessageLength = 0;
                            output = new StringBuilder(messageLength);
                        } catch (NumberFormatException e) {
                            throw new SteganographyException("Invalid message format: could not parse length", e);
                        }
                    } else {
                        currentMessageLength++;
                        output.append((char) extractedValue);
                    }
                    extractedValue = 0;
                    bitsInByte = 0;
                }

                extractedValue = extractedValue << 1;
                pointerService.nextPixel(point, image);
                bitsInByte++;
            }

            return output.toString();
        } catch (IOException e) {
            throw new SteganographyException("Error reading image: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            if (e instanceof SteganographyException) {
                throw e;
            }
            throw new SteganographyException("Error extracting message: " + e.getMessage(), e);
        }
    }
}
