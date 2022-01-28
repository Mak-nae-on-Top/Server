package com.maknaeontop.communication;

import com.maknaeontop.Beacon;
import com.maknaeontop.User;
import com.maknaeontop.communication.mapper.UserMapper;
import com.maknaeontop.communication.sevice.*;
import com.maknaeontop.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class ServerController {
    private UserService userService;
    private BeaconService beaconService;
    private PopulationService populationService;
    private RoomService roomService;
    private BuildingService buildingService;
    private Location location;

    // Constructor
    public ServerController(UserService userService, BeaconService beaconService){
        this.userService = userService;
        this.beaconService = beaconService;
    }

    @PostMapping("/app/location")
    public float[] estimateLocation(@RequestBody List<Beacon> beaconList){
        // TODO: get user id and uuid
        String id = "";
        String uuid = beaconList.get(0).getUuid();

        // load location using UUID, major and minor
        for(Beacon beacon : beaconList){
            HashMap<String, Object> location = beaconService.getLocation(beacon.getUuid(), beacon.getMajor(), beacon.getMinor());
            beacon.setLocation((float)location.get("x"), (float)location.get("y"), (float)location.get("z"));
        }
        // find user location
        float[] userLocation = location.findUserLocation(beaconList);

        // save user location on DB
        populationService.insertUserLocation(uuid, id, userLocation[0], userLocation[1], userLocation[2]);

        // return the user location list of the building
        return userLocation;
    }

    @PostMapping("/app/manager/saveMap")
    public boolean saveMap(@RequestBody Object obj){
        // TODO: convert to map and save the map on the storage
        return false;
    }

    @PostMapping("/app/manager/enterBeaconLocation")
    public boolean enterBeaconLocation(@RequestBody List<Beacon> beaconList){
        for(Beacon beacon : beaconList){
            beaconService.addBeacon(beacon.getUuid(), beacon.getMajor(), beacon.getMinor(), beacon.getX(), beacon.getY(), beacon.getZ());
        }
        return true;
    }

    @PostMapping("/app/login")
    public boolean login(@RequestBody User user){
        return userService.validateUser(user);
    }

    @PostMapping("/app/join")
    public boolean join(@RequestBody User user){
        return userService.addUser(user);
    }

    @PostMapping("/app/event")
    private String event(String msg){
        // TODO: send message to application
        return msg;
    }

    @PostMapping("/fireAlarm")
    public String communicateWithFireAlarm(@RequestBody String msg){
        // TODO: send message to application
        return null;
    }
}
