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
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("app")
@AllArgsConstructor
public class AppController {
    private final UserService userService;
    private final BeaconService beaconService;
    private final PopulationService populationService;
    private RoomService roomService;
    private BuildingService buildingService;
    private final Location location = Location.getInstance();
    private final Response response = new Response();
    private final BlueprintUtil blueprintUtil = new BlueprintUtil();
    private final FloorService floorService;
    private static final MessageRepository messageRepository = new MessageRepository();
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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

    @PostMapping("/location")
    public String estimateLocation(@RequestBody List<Beacon> beaconList, HttpServletRequest request) {
        final String deviceId = request.getHeader("Device");
        List<Beacon> beaconListIncludeLocation = beaconService.loadBeaconLocation(beaconList);
        HashMap<String, Float> userLocation = location.findUserLocation(beaconListIncludeLocation);
        String uuid = beaconListIncludeLocation.get(0).getUuid();
        int floor = beaconListIncludeLocation.get(0).getFloor();

        List<HashMap<String, Float>> locationList = populationService.selectLocationByUuid(uuid, floor, deviceId);
        locationList.add(0,userLocation);

        Population population = new Population(uuid, floor);
        population.setLocationList(locationList);

        //populationService.insertUserLocation(deviceId, uuid, userLocation.get("x"), userLocation.get("y"), floor);

        return response.locationResponse(population);
    }

    @PostMapping(value = "/loadMap")
    public String loadMap(@RequestBody LoadMapDto loadMapDto) throws IOException {
        String base64Image = blueprintUtil.loadImage(loadMapDto.getUuid(), loadMapDto.getFloor());
        HashMap<String, Integer> heightAndWidth = floorService.selectHeightsAndWidthsByFloor(loadMapDto.getUuid(), loadMapDto.getFloor());
        return response.base64Response("success", heightAndWidth, base64Image);
    }

    @PostMapping("/manager/saveMap")
    public String saveMap(@RequestBody Base64Image base64Image) throws IOException {
        blueprintUtil.saveImage(base64Image);
        floorService.insertImageInfo(base64Image.getUuid(), base64Image.getFloor(), base64Image.getImageHeight(), base64Image.getImageWidth());
        return response.statusResponse("success","image save success");
    }

    @PostMapping("/manager/enterRoomName")
    public String enterRoomName(@RequestBody Room room){
        roomService.insertRoom(room);
        return response.statusResponse("success", "saved successfully");
    }

    @PostMapping("/manager/enterBeaconLocation")
    public String enterBeaconLocation(@RequestBody List<Beacon> beaconList){
        beaconService.addBeaconList(beaconList);
        return response.statusResponse("success","saved beacon info");
    }

    @PostMapping("/manager/loadAllMap")
    public List<MapDto> loadAllMap(HttpServletRequest request) throws IOException {
        String id = jwtTokenUtil.getIdFromToken(request);
        List<HashMap<String, Object>> buildingList = buildingService.selectByManager(id);
        return blueprintUtil.loadAllMap(buildingList);
    }

    @GetMapping("/createWebsocketRoom")
    public void createRoom(@RequestParam String uuid){
        WebSocketRoom webSocketRoom = new WebSocketRoom();
        messageRepository.createRoom(uuid, webSocketRoom);
    }
}
