package com.maknaeontop.blueprint;

import com.maknaeontop.dto.Base64Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BlueprintUtil {
    private final String PATHPREFIX = "/home/ubuntu/image/blueprint/";
    private final String EXTENSION = ".jpg";
    private final Base64.Decoder decoder = Base64.getDecoder();

    public void saveImage(Base64Image base64Image) throws IOException {
        String pathName = PATHPREFIX + base64Image.getUuid() + "_" + base64Image.getFloor() + EXTENSION;
        byte[] test = decoder.decode(base64Image.getBase64().getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(test);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        ImageIO.write(bufferedImage, "png", new File(pathName));
    }
}
