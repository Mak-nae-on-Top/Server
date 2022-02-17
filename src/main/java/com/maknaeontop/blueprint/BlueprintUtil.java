package com.maknaeontop.blueprint;

import com.maknaeontop.dto.Base64Image;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class BlueprintUtil {
    private final String PATH_PREFIX = "/home/ubuntu/image/blueprint/";
    private final String EXTENSION = ".txt";

    private final ProcessBuilder processBuilder = new ProcessBuilder();

    public boolean saveImage(Base64Image base64Image) throws IOException{
        String pathName = PATH_PREFIX + base64Image.getUuid() + "_" + base64Image.getFloor() + EXTENSION;
        File file = new File(pathName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(base64Image.getBase64());
        writer.close();

        if(!createMap(pathName)){
            file.delete();
            return false;
        }
        return true;
    }

    public boolean deleteMap(String uuid, String floor){
        String pathName = PATH_PREFIX + uuid + "_" + floor + EXTENSION;
        File file = new File(pathName);
        if(file.exists()){
            file.delete();
            return false;
        }
        return true;
    }

    public String loadImage(String uuid, int floor) throws IOException {
        Path path = Paths.get(PATH_PREFIX + uuid + "_" + floor + EXTENSION);
        return new String(Files.readAllBytes(path));
    }

    public boolean createMap(String pathName) {
        try {
            processBuilder.executeConvertImageToMapModule(pathName);
        }catch (Exception e){
            System.out.println("create map error: "+e.toString());
            return false;
        }
        return true;
    }

    public String getRoute(List<HashMap<String, Float>> location, List<HashMap<String, Object>> roomList) throws IOException, InterruptedException {
        String locationString = location.toString().trim();
        String roomListString = roomList.toString().trim();
        return processBuilder.executeFindRouteModule(locationString, roomListString);
    }
}
