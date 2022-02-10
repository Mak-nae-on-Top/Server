package com.maknaeontop.communication.controller;

import com.maknaeontop.blueprint.BlueprintUtil;
import com.maknaeontop.communication.JsonBuilder;
import com.maknaeontop.communication.jwt.JwtTokenUtil;
import com.maknaeontop.dto.*;
import com.maknaeontop.communication.sevice.*;
import com.maknaeontop.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("app")
public class AppController {
    private final String PATHPREFIX = "/home/ubuntu/image/blueprint/";
    //private final String PATHPREFIX = "C:/Users/namu/Desktop/test/";
    private final String EXTENSION = ".jpg";

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
    public String estimateLocation(@RequestBody List<Beacon> beaconList, HttpServletRequest request){
        final String id = request.getHeader("Device");
        String uuid = beaconList.get(0).getUuid();

        // load location using UUID, major and minor
        List<Beacon> beaconListIncludeLocation = beaconService.loadBeaconLocation(beaconList);

        // find user location
        float[] userLocation = location.findUserLocation(beaconListIncludeLocation);

        // save user location on DB
        populationService.insertUserLocation(id, uuid, userLocation[0], userLocation[1], 0f/* userLocation[2]*/);

        // return the user location list of the building
        return jsonBuilder.locationResponse(userLocation);
    }

    @PostMapping(value = "/loadMap")
    public ResponseEntity<?> loadMap(@RequestBody LoadMap loadMap) throws IOException {
        return blueprintUtil.loadImage(loadMap);
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
