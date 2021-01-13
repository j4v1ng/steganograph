package com.javing.steganograph.service.textbased;

import com.javing.steganograph.service.support.PointerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;

@AllArgsConstructor
@Service
public class ImageStegService {

    private final PointerService pointerService;

    public byte[] steg(final byte[] payloadImage, final byte[] wrapperImage) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        write(processImage(payloadImage, wrapperImage), "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private BufferedImage processImage(byte[] payloadImage, byte[] wrapperImage) throws IOException {

        BufferedImage image = read(new ByteArrayInputStream(wrapperImage));

        Point point = new Point(0, 0);

        if (payloadImage.length * 8 > (image.getWidth() * image.getHeight())) {
            throw new RuntimeException("There won't be enough space to store this message!");
        }

        for (int megaByte : payloadImage) {
            for (int index = 0; index < 8; index++) {
                if ((megaByte & 128) == 128) {
                    image.setRGB(point.x, point.y, setLeastSignificantBit(image.getRGB(point.x, point.y), true));
                } else {
                    image.setRGB(point.x, point.y, setLeastSignificantBit(image.getRGB(point.x, point.y), false));
                }
                megaByte = megaByte << 1;
                pointerService.moveToNextPixel(point, image);
            }
        }

        return image;
    }

    protected int setLeastSignificantBit(int bit, boolean setToOne) {

        bit = (bit >> 1);
        bit = (bit << 1);
        if (setToOne) {
            bit++;
        }
        return bit;
    }
}
