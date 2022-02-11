package com.maknaeontop.blueprint;

import com.maknaeontop.dto.Base64Image;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class BlueprintUtil {
    private final String PATHPREFIX = "/home/ubuntu/image/blueprint/";
    //private final String PATHPREFIX = "C:/Users/namu/Desktop/test/";
    private final String EXTENSION = ".txt";
    private final Base64.Decoder decoder = Base64.getDecoder();

    /*
    public void saveImage(Base64Image base64Image) throws IOException {
        String pathName = PATHPREFIX + base64Image.getUuid() + "_" + base64Image.getFloor() + EXTENSION;
        byte[] decodedImageByte = decoder.decode(base64Image.getBase64().getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedImageByte);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        ImageIO.write(bufferedImage, "png", new File(pathName));
    }
     */

    public void saveImage(Base64Image base64Image) throws IOException{
        String pathName = PATHPREFIX + base64Image.getUuid() + "_" + base64Image.getFloor() + EXTENSION;
        File file = new File(pathName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(base64Image.getBase64());
        writer.close();
    }
/*
    public ResponseEntity<Resource> loadImage(String uuid, int floor) throws IOException {
        Path path = Paths.get(PATHPREFIX + uuid + "_" + floor + EXTENSION);
        String contentType = Files.probeContentType(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("filename", StandardCharsets.UTF_8).build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
*/
    public String loadImage(String uuid, int floor) throws IOException {
        Path path = Paths.get(PATHPREFIX + uuid + "_" + floor + EXTENSION);
        return new String(Files.readAllBytes(path));
    }
}
