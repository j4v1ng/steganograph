package com.javing.steganograph.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

@AllArgsConstructor
@Service
public class ImageProcessingService {

    private static final String SEPARATOR = ":";
    private final PointerService pointerService;

    public BufferedImage processImage(String payload, byte[] imageInput) throws IOException {

        BufferedImage image = read(new ByteArrayInputStream(imageInput));

        Point point = new Point(0, 0);
        payload = payload.length() + SEPARATOR + payload;
        byte[] messageBytes = payload.getBytes();

        if (payload.length() * 8 > (image.getWidth() * image.getHeight())) {
            throw new RuntimeException("The image is too small to store the payload!");
        }

        for (int currentByte : messageBytes) {
            for (int index = 0; index < 8; index++) {
                if ((currentByte & 128) == 128) {
                    image.setRGB(point.x, point.y, setLeastSignificantBit(image.getRGB(point.x, point.y), true));
                } else {
                    image.setRGB(point.x, point.y, setLeastSignificantBit(image.getRGB(point.x, point.y), false));
                }
                currentByte = currentByte << 1;
                pointerService.nextPixel(point, image);
            }
        }

        return image;
    }

    private int setLeastSignificantBit(int bit, boolean reset) {

        bit = (bit >> 1);
        bit = (bit << 1);
        if (reset) {
            bit++;
        }
        return bit;
    }

}
