package com.javing.steganograph.service;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class PointerService {

    public void nextPixel(Point point, BufferedImage image) {

        if (point.x == (image.getWidth() - 1)) {
            point.x = -1;
            point.y++;
        }
        point.x++;

        if (point.y == image.getHeight()) {
            throw new RuntimeException("Pointer out of bounds");
        }
    }
}
