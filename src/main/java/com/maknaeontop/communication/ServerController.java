package com.maknaeontop.communication;

import com.maknaeontop.Beacon;
import com.maknaeontop.User;
import com.maknaeontop.communication.mapper.UserMapper;
import com.maknaeontop.communication.sevice.BeaconService;
import com.maknaeontop.communication.sevice.UserService;
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
    private Location location;

    public ServerController(UserService userService, BeaconService beaconService){
        this.userService = userService;
        this.beaconService = beaconService;
    }

    @PostMapping("/app/location")
    public HashMap<String, Object> estimateLocation(@RequestBody List<Beacon> beaconList){
        // load location using UUID, major and minor
        for(Beacon beacon : beaconList){
            HashMap<String, Object> location = beaconService.getLocation(beacon.getUuid(), beacon.getMajor(), beacon.getMinor());
            beacon.setLocation((float)location.get("x"), (float)location.get("y"), (float)location.get("z"));
        }
        // find user location
        float[] userLocation = location.findUserLocation(beaconList);

        // TODO: save user location data on database
        // TODO: return the user location list of the building
        return null;
    }

    @PostMapping("/app/manager/saveMap")
    public boolean saveMap(@RequestBody Object obj){
        // TODO: save the map on the storage
        return false;
    }

    @PostMapping("/app/manager/enterBeaconLocation")
    public boolean enterBeaconLocation(@RequestBody List<Beacon> beaconList){
        // TODO: save the beacon location on database
        return false;
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
        return msg;
    }

    @PostMapping("/fireAlarm")
    public String communicateWithFireAlarm(@RequestBody String msg){
        return event(msg);
    }
}
