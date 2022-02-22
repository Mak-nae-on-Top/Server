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

    /**
     * Method to store the base64 encoded image as txt on the server.
     *
     * @param base64Image   the base64 encoded image
     * @return              true if image transformation is successful
     * @throws IOException
     */
    public boolean saveImage(Base64Image base64Image) throws IOException{
        String pathName = getImageFilePath(base64Image.getUuid(), base64Image.getFloor());
        File file = new File(pathName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(base64Image.getBase64());
        writer.close();

        if(!createMap(pathName)){
            //file.delete();
            //return false;
            return true;
        }
        return true;
    }

    /**
     * Method to delete a map of a corresponding building floor from the server.
     *
     * @param uuid      the building uuid that user located
     * @param floor     the floor that user located
     * @return          true if deleting map is successful
     */
    public boolean deleteMap(String uuid, String floor){
        String pathName = getImageFilePath(uuid, floor);
        File file = new File(pathName);
        if(file.exists()){
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * Method to load a map of a corresponding building floor from the server.
     *
     * @param uuid      the building uuid where the user is located
     * @param floor     the floor where the user is located
     * @return          the base64 encoded image file
     * @throws IOException
     */
    public String loadImage(String uuid, String floor) throws IOException {
        Path path = Paths.get(getImageFilePath(uuid, floor));
        return new String(Files.readAllBytes(path));
    }

    /**
     * Method to call the processBuilder to create the map.
     *
     * @param pathName  the path where the image is saved
     * @return          true if creating map is successful
     */
    public boolean createMap(String pathName) {
        try {
            processBuilder.executeConvertImageToMapModule(pathName);
        }catch (Exception e){
            System.out.println("create map error: "+e.toString());
            return false;
        }
        return true;
    }

    /**
     * Method to remove spaces in each parameter and executing processBuilder.
     *
     * @param location  the users location list
     * @param roomList  the room list that the user wants to go
     * @param uuid      the building uuid where the user is located
     * @param floor     the floor where the user is located
     * @return          the coordinates of the route
     * @throws IOException
     * @throws InterruptedException
     */
    public String getRoute(List<String> location, List<String> roomList, String uuid, String floor) throws IOException, InterruptedException {
        String locationString = location.toString().replace(" ","");
        String roomListString = roomList.toString().replace(" ","");
        //String imagePathString = getImageFilePath(uuid, floor);
        String imagePathString = "C:/Users/namu/Desktop/test/image.txt";
        return processBuilder.executeFindRouteModule(locationString, roomListString, imagePathString);
    }

    /**
     * Method to get the image file path using uuid and floor.
     *
     * @param uuid      the building uuid where the user is located
     * @param floor     the floor where the user is located
     * @return          the file path
     */
    private String getImageFilePath(String uuid, String floor){
        return PATH_PREFIX + uuid + "_" + floor + EXTENSION;
    }
}
