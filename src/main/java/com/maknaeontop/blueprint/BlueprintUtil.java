package com.maknaeontop.blueprint;

import com.maknaeontop.dto.Base64Image;
import com.maknaeontop.dto.MapDto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class BlueprintUtil {
    private final String PATH_PREFIX = "/home/ubuntu/image/blueprint/";
    //private final String PATHPREFIX = "C:/Users/namu/Desktop/test/";
    private final String EXTENSION = ".txt";

    private final PythonProcessBuilder pythonProcessBuilder = new PythonProcessBuilder();

    public void saveImage(Base64Image base64Image) throws IOException{
        String pathName = PATH_PREFIX + base64Image.getUuid() + "_" + base64Image.getFloor() + EXTENSION;
        File file = new File(pathName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(base64Image.getBase64());
        writer.close();
    }

    public String loadImage(String uuid, String floor) throws IOException {
        Path path = Paths.get(PATH_PREFIX + uuid + "_" + floor + EXTENSION);
        return new String(Files.readAllBytes(path));
    }

    public List<MapDto> loadAllMap(List<HashMap<String, Object>> buildingList) throws IOException {
        List<MapDto> mapList = new ArrayList<>();

        for(HashMap<String, Object> buildingHashMap : buildingList){
            String uuid = buildingHashMap.get("uuid").toString();
            String buildingName = buildingHashMap.get("name").toString();
            int lowestFloor = (int) buildingHashMap.get("lowest_floor");
            int highestFloor = (int) buildingHashMap.get("highest_floor");

            try{
                for(int i=lowestFloor; i<=highestFloor;i++){
                    MapDto mapDto = new MapDto(uuid,buildingName, Integer.toString(i), loadImage(uuid, Integer.toString(i)));
                    mapList.add(mapDto);
                }
            }catch (NullPointerException e){
                continue;
            }
        }
        return mapList;
    }

    // TODO:
    public void buildMap(String fileName) throws IOException, InterruptedException {
        pythonProcessBuilder.executeMapBuilder(fileName);
    }
}
