package com.maknaeontop.communication.controller;

import com.maknaeontop.blueprint.BlueprintUtil;
import com.maknaeontop.communication.JsonBuilder;
import com.maknaeontop.communication.jwt.JwtTokenUtil;
import com.maknaeontop.dto.*;
import com.maknaeontop.communication.sevice.*;
import com.maknaeontop.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("app")
public class AppController {
    private final UserService userService;
    private final BeaconService beaconService;
    private final PopulationService populationService;
    private RoomService roomService;
    private BuildingService buildingService;
    private final Location location = Location.getInstance();
    private final JsonBuilder jsonBuilder = new JsonBuilder();
    private final BlueprintUtil blueprintUtil = new BlueprintUtil();
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Constructor
    public AppController(UserService userService, BeaconService beaconService, PopulationService populationService){
        this.userService = userService;
        this.beaconService = beaconService;
        this.populationService = populationService;
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        String pwInDatabase = userService.selectPwUsingId(user.getId());
        if(pwInDatabase != null){
            if(pwInDatabase.equals(user.getPassword())){
                String token = jwtTokenUtil.generateToken(user);
                return jsonBuilder.tokenResponse("success","login complete",token);
            }
            return jsonBuilder.tokenResponse("fail","password does not match","");
        }
        return jsonBuilder.tokenResponse("fail","id does not exist","");
    }

    @PostMapping("/join")
    public String join(@RequestBody User user) {
        if (user.getPassword().equals(user.getPassword2())) {
            if(userService.addUser(user)){
                return jsonBuilder.statusResponse("success","registration complete");
            }
            return jsonBuilder.statusResponse("fail","id is already exist");
        }
        return jsonBuilder.statusResponse("fail","password and password2 do not match");
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

        populationService.insertUserLocation(deviceId, uuid, userLocation.get("x"), userLocation.get("y"), floor);

        return jsonBuilder.locationResponse(population);
    }

    @PostMapping(value = "/loadMap")
    public String loadMap(@RequestBody UuidAndFloor uuidAndFloor) throws IOException {
        String base64Image = blueprintUtil.loadImage(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
        HashMap<String, Float> maxXY = beaconService.selectMaxXYByUuidAndFloor(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
        return jsonBuilder.base64Response("success", maxXY.get("x"), maxXY.get("y"), base64Image);
    }

    @PostMapping("/manager/saveMap")
    public String saveMap(@RequestBody Base64Image base64Image) throws IOException {
        blueprintUtil.saveImage(base64Image);
        return jsonBuilder.statusResponse("success","image save success");
    }

    @PostMapping("/manager/enterRoomName")
    public String enterRoomName(@RequestBody Room room){
        roomService.insertRoom(room);
        return jsonBuilder.statusResponse("success", "saved successfully");
    }

    @PostMapping("/manager/enterBeaconLocation")
    public String enterBeaconLocation(@RequestBody List<Beacon> beaconList){
        beaconService.addBeaconList(beaconList);
        return jsonBuilder.statusResponse("success","saved beacon info");
    }

    @PostMapping("/event")
    private String event(String msg){
        // TODO: send message to application
        return msg;
    }
}
