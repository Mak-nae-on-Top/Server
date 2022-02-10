package com.maknaeontop.blueprint;

import com.maknaeontop.dto.Base64Image;
import com.maknaeontop.dto.LoadMap;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public ResponseEntity<Resource> loadImage(LoadMap loadMap) throws IOException {
        Path path = Paths.get(PATHPREFIX + loadMap.getUuid() + "_" + loadMap.getFloor() + EXTENSION);
        String contentType = Files.probeContentType(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("filename", StandardCharsets.UTF_8).build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
