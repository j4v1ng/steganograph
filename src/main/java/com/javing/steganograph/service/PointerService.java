package com.javing.steganograph.service;

import com.javing.steganograph.exception.SteganographyException;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Service for navigating through image pixels.
 */
@Service
public class PointerService {

    /**
     * Moves the pointer to the next pixel in the image.
     * If the pointer reaches the end of a row, it moves to the beginning of the next row.
     *
     * @param point the current position
     * @param image the image being processed
     * @throws SteganographyException if the pointer goes beyond the image boundaries
     */
    public void nextPixel(Point point, BufferedImage image) {
        if (point.x == (image.getWidth() - 1)) {
            point.x = -1;
            point.y++;
        }
        point.x++;

        if (point.y == image.getHeight()) {
            throw new SteganographyException("Pointer out of bounds - reached the end of the image");
        }
    }
}
