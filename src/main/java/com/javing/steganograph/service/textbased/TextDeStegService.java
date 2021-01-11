package com.javing.steganograph.service.textbased;

import com.javing.steganograph.service.support.PointerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.lang.Integer.parseInt;
import static javax.imageio.ImageIO.read;

@AllArgsConstructor
@Service
public class TextDeStegService {

    private final PointerService pointerService;

    public String deSteg(byte[] fileBytes) throws IOException {

        BufferedImage image = read(new ByteArrayInputStream(fileBytes));

        StringBuilder output = new StringBuilder();
        int bitsInByte = 1;
        int extractedValue = 0;
        boolean gotLength = false;
        int messageLength = 0;
        int currentMessageLength = 0;
        Point point = new Point(0, 0);

        while (!gotLength || currentMessageLength < messageLength) {

            if ((image.getRGB(point.x, point.y) & 1) == 1) {
                extractedValue += 1;
            }

            if (bitsInByte == 8) {

                if (!gotLength && ':' == extractedValue) {
                    gotLength = true;
                    messageLength = parseInt(output.toString());
                    currentMessageLength = 0;
                    output = new StringBuilder(messageLength);
                } else {
                    currentMessageLength++;
                    output.append((char) extractedValue);
                }
                extractedValue = 0;
                bitsInByte = 0;
            }

            extractedValue = extractedValue << 1;
            pointerService.moveToNextPixel(point, image);
            bitsInByte++;
        }

        return output.toString();
    }
}
