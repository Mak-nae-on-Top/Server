package com.maknaeontop.communication.controller;

import com.maknaeontop.blueprint.BlueprintUtil;
import com.maknaeontop.communication.Response;
import com.maknaeontop.communication.jwt.JwtTokenUtil;
import com.maknaeontop.communication.websocket.MessageRepository;
import com.maknaeontop.communication.websocket.WebSocketRoom;
import com.maknaeontop.dto.*;
import com.maknaeontop.communication.sevice.*;
import com.maknaeontop.location.Location;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AppController is the controller for the functions
 * that communicate with the app.
 */
@RestController
@RequestMapping("app")
@AllArgsConstructor
public class AppController {
    private final UserService userService;
    private final BeaconService beaconService;
    private final PopulationService populationService;
    private final RoomService roomService;
    private final BuildingService buildingService;
    private final TrilaterationModelService trilaterationModelService;
    private final Location location = Location.getInstance();
    private final Response response = new Response();
    private final BlueprintUtil blueprintUtil = new BlueprintUtil();
    private static final MessageRepository messageRepository = new MessageRepository();
    @Autowired
    private final JwtTokenUtil jwtTokenUtil;

    /**
     *  Method to log in by receiving id and password
     *
     * @param user  the user information including id and password
     * @return      status and message in json format
     */
    @PostMapping("/login")
    public String login(@RequestBody User user){
        String pwInDatabase = userService.selectPwUsingId(user.getId());
        if(pwInDatabase != null){
            if(pwInDatabase.equals(user.getPassword())){
                String token = jwtTokenUtil.generateToken(user);
                return response.tokenResponse("success","login complete",token);
            }
            return response.tokenResponse("fail","password does not match","");
        }
        return response.tokenResponse("fail","id does not exist","");
    }

    /**
     * Method that receives id, password, and password2,
     * and sign up if password and password2 are the same and the ID does not already exist
     *
     * @param user  the user information including id, password, and password2
     * @return      status and message in json format
     */
    @PostMapping("/join")
    public String join(@RequestBody User user) {
        if (user.getPassword().equals(user.getPassword2())) {
            if(userService.addUser(user)){
                return response.statusResponse("success","registration complete");
            }
            return response.statusResponse("fail","id is already exist");
        }
        return response.statusResponse("fail","password and password2 do not match");
    }

    /**
     * Method to measure the user's location based on the beacon list obtained by the app
     *
     * @param beaconList    list of beacons obtained by the app, sorted by distance from the user
     * @param request       HttpServletRequest to get device id
     * @return              uuid, floor and the coordinates of the location of all users
     *                      on the same building and floor as user.
     *                      0th in the list is the user's location
     */
    @PostMapping("/location")
    public String estimateLocation(@RequestBody List<Beacon> beaconList, HttpServletRequest request) {
        final String deviceId = request.getHeader("Device");

        String uuid = beaconList.get(0).getUuid();

        // Get and store the xy information of beacons and extract uuid and floor
        // Get only 3 coordinates of the beacons on the same floor as the 0th beacon. Returns null if less than 3
        List<Beacon> beaconListIncludeLocation = beaconService.loadBeaconLocation(uuid, beaconList);
        // fail if list size is less than 3
        if(beaconListIncludeLocation == null) return response.statusResponse("fail", "beacon wasn't searched enough");
        int floor = Integer.parseInt(beaconListIncludeLocation.get(0).getFloor());

        //Calculate user location with model
        HashMap<String, Float> constants = trilaterationModelService.selectConstants(uuid);
        HashMap<String, Float> userLocation;
        if(constants != null){
            userLocation = location.calculateUserLocationWithModel(constants.get("a"), constants.get("b"), beaconListIncludeLocation);
        }else {
            userLocation = location.calculateUserLocation(beaconListIncludeLocation);
        }
        List<HashMap<String, Float>> locationList = populationService.selectCoordinateAfterInsert(deviceId, uuid, userLocation.get("x"), userLocation.get("y"), floor);

        Population population = new Population("success", uuid, floor);
        population.setLocation_list(locationList);

        return response.locationResponse(population);
    }

    /**
     * Method that takes the uuid and floor of a building and returns a map of that location.
     *
     * @param uuidAndFloor  uuid and floor of user
     * @return              image encoded in base64
     * @throws IOException
     */
    @PostMapping(value = "/loadMap")
    public String loadMap(@RequestBody UuidAndFloor uuidAndFloor) throws IOException {
        String base64Image = blueprintUtil.loadImage(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
        return response.base64Response("success", base64Image);
    }

    /**
     * Method that returns an optimized route from the user's location to the destination.
     *
     * @param routeRequest  uuid and floor of user and destination that user wants to go
     * @param request       HttpServletRequest to get device id
     * @return              array of coordinates in the optimized route
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping("/loadRoute")
    public String loadRoute(@RequestBody RouteRequest routeRequest, HttpServletRequest request) throws IOException, InterruptedException {
        final String deviceId = request.getHeader("Device");

        //Get all users in the same building/floor as the user, but make the user 0
        List<Coordinate> location = populationService.selectCoordinateInSameFloor(routeRequest.getUuid(), Integer.parseInt(routeRequest.getFloor()), deviceId);
        // Get destination list - through user uuid, floor, destination name
        List<Coordinate> roomList = roomService.selectCoordinateByUuidAndFloorAndRoomName(routeRequest);

        List<String> locationArray = new ArrayList<>();
        List<String> roomArray = new ArrayList<>();
        for(Coordinate coordinate:location){
            locationArray.add(coordinate.toStringCoordinate());
        }
        for(Coordinate coordinate:roomList){
            roomArray.add(coordinate.toStringCoordinate());
        }

        List<HashMap<String, Float>> coordinates = blueprintUtil.getRouteDemo(locationArray, roomArray, routeRequest.getUuid(), routeRequest.getFloor());

        return response.routeResponse(coordinates);
        //return coordinates;
    }

    /**
     * Method to receive a base64-encoded blueprint image, convert it to a map,
     * and save it to the server.
     *
     * @param base64Image   base64-encoded blueprint image
     * @param request       HttpServletRequest to get id
     * @return              status and message in json format
     * @throws IOException
     */
    @PostMapping("/manager/saveMap")
    public String saveMap(@RequestBody Base64Image base64Image, HttpServletRequest request) throws IOException {
        String id = jwtTokenUtil.getIdFromToken(request);
        HashMap<String, Integer> floorRange = buildingService.selectFloorRangeByUuid(base64Image.getUuid());
        if(floorRange == null){ // In the case of a newly registered building
            messageRepository.createRoom(base64Image.getUuid(), new WebSocketRoom());
        }else { // When modifying an existing building
            if(floorRange.get("lowest_floor") > Integer.parseInt(base64Image.getFloor())){
                buildingService.updateLowestFloor(base64Image.getUuid(), base64Image.getFloor());
            }
            if(floorRange.get("highest_floor") < Integer.parseInt(base64Image.getFloor())){
                buildingService.updateHighestFloor(base64Image.getUuid(), base64Image.getFloor());
            }
        }

        if(!blueprintUtil.saveImage(base64Image)){
            return response.statusResponse("fail","fail to convert image to map");
        }
        buildingService.insertBuilding(base64Image, id);
        return response.statusResponse("success","image save success");
    }

    /**
     * Method to set the name and coordinates of a room on the corresponding floor of the building.
     *
     * @param roomListOnFloor   uuid, floor and room list contains coordinate and room name.
     * @return                  status and message in json format
     */
    @PostMapping("/manager/modifyCoordinates")
    public String enterRoomName(@RequestBody RoomListOnFloor roomListOnFloor){
        roomService.insertRoom(roomListOnFloor);

        return response.statusResponse("success", "saved successfully");
    }

    /**
     * Method to delete all information in the floor.
     *
     * @param uuidAndFloor  uuid and floor
     * @return              status and message in json format
     */
    @PostMapping("/manager/deleteFloor")
    public String deleteFloor(@RequestBody UuidAndFloor uuidAndFloor){
        // Find and delete images
        blueprintUtil.deleteMap(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
        // delete from db
        beaconService.deleteByUuidAndFloor(uuidAndFloor);       // beacon table
        buildingService.deleteFloor(uuidAndFloor);              // building table
        roomService.deleteByUuidAndFloor(uuidAndFloor);         // room table

        return response.statusResponse("success", "floor data is deleted");
    }

    /**
     * Method to register information including location of beacon.
     * @param enterBeaconRequest    uuid, floor and beacon's information list
     *                              including major, minor, coordinate
     * @return                      status and message in json format
     */
    @PostMapping("/manager/enterBeaconLocation")
    public String enterBeaconLocation(@RequestBody EnterBeaconRequest enterBeaconRequest){
        beaconService.addBeaconList(enterBeaconRequest);

        return response.statusResponse("success","saved beacon info");
    }

    /**
     * Method that returns all information including floor maps of all buildings
     * where the user id is registered as an manager.
     *
     * @param request   HttpServletRequest to get id
     * @return          all information of buildings
     */
    @PostMapping("/manager/loadAllMap")
    public String loadAllMap(HttpServletRequest request) {
        String id = jwtTokenUtil.getIdFromToken(request);
        List<HashMap<String, Object>> buildingList = buildingService.selectByManager(id);   // Get Building List
        List<FloorInfo> mapList = new ArrayList<>();

        for(HashMap<String, Object> buildingHashMap : buildingList){    // Go around the building and get the uuid, building name, lowest floor, highest floor
            String uuid = buildingHashMap.get("uuid").toString();
            String buildingName = buildingHashMap.get("name").toString();
            int lowestFloor = (int) buildingHashMap.get("lowest_floor");
            int highestFloor = (int) buildingHashMap.get("highest_floor");

            for(int floor=lowestFloor; floor<=highestFloor;floor++){
                try {
                    List<HashMap<String, Object>> beaconInfo = beaconService.selectByUuidAndFloor(uuid, floor); //Get beacon list
                    List<HashMap<String, Object>> coordinate = roomService.selectByUuidAndFloor(uuid, floor);   // Get room information (x, y, roomName, id) for each floor
                    String base64Image = blueprintUtil.loadImage(uuid, Integer.toString(floor));
                    FloorInfo floorInfo = new FloorInfo(uuid, buildingName, Integer.toString(floor), base64Image, coordinate, beaconInfo); // Save all
                    mapList.add(floorInfo);
                }catch (NullPointerException | IOException e){
                    continue;
                }
            }
        }
        return response.allMapResponse(mapList);
    }

    /**
     * Method to obtain a model for correcting position measurements using trilateration.
     *
     * @param coordinateAndRangedBeacons     list of beacons obtained by the app
     * @return                  status and message in json format
     */
    @PostMapping("/manager/init")
    public String initBeacon(@RequestBody InitRequest coordinateAndRangedBeacons){
        List<List<Beacon>> testList = new ArrayList<>();
        String uuid = coordinateAndRangedBeacons.getRangedBeacons().get(0).get(0).getUuid();

        for(int i=0;i<coordinateAndRangedBeacons.getRangedBeacons().size();i++){
            List<Beacon> beaconList = coordinateAndRangedBeacons.getRangedBeacons().get(i);
            List<Beacon> beaconListIncludeLocation = beaconService.loadBeaconLocation(beaconList.get(0).getUuid(), beaconList);
            if(beaconListIncludeLocation == null){
                continue;
            }
            testList.add(beaconListIncludeLocation);
        }
        HashMap<String, Float> modelConstant = location.createModel(coordinateAndRangedBeacons.getX(), coordinateAndRangedBeacons.getY(), testList);
        trilaterationModelService.insertConstants(uuid, modelConstant.get("a"), modelConstant.get("b"));

        return response.statusResponse("success", "initialized trilateration model successfully");
    }

    /**
     * Method to create websocket room
     * @param uuid  building uuid
     */
    @GetMapping("/admin/createWebsocketRoom")
    public String createWebsocketRoom(@RequestParam String uuid){
        messageRepository.createRoom(uuid, new WebSocketRoom());
        return response.statusResponse("success", "successfully created websocket room");
    }
}
