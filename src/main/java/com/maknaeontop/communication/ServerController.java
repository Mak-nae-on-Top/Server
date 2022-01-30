package com.maknaeontop.communication;

import com.maknaeontop.Beacon;
import com.maknaeontop.User;
import com.maknaeontop.communication.mapper.UserMapper;
import com.maknaeontop.communication.sevice.*;
import com.maknaeontop.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RestController
public class ServerController {
    private UserService userService;
    private BeaconService beaconService;
    private PopulationService populationService;
    private RoomService roomService;
    private BuildingService buildingService;
    private Location location = Location.getInstance();

    // Constructor
    public ServerController(UserService userService, BeaconService beaconService, PopulationService populationService){
        this.userService = userService;
        this.beaconService = beaconService;
        this.populationService = populationService;
    }

    @PostMapping("/app/location")
    public float[] estimateLocation(@RequestBody List<Beacon> beaconList, HttpSession session){
        // TODO: change 2D to 3D
        String id = String.valueOf(session.getAttribute("id"));
        String uuid = beaconList.get(0).getUuid();

        // load location using UUID, major and minor
        for(Beacon beacon : beaconList){
            HashMap<String, Object> location = beaconService.getLocation(beacon.getUuid(), beacon.getMajor(), beacon.getMinor());
            beacon.setLocation((float)location.get("x"), (float)location.get("y"), (float)location.get("z"));
        }
        // find user location
        float[] userLocation = location.findUserLocation(beaconList);

        // save user location on DB
        populationService.insertUserLocation(id, uuid, userLocation[0], userLocation[1], 12.01f/* userLocation[2]*/);

        // return the user location list of the building
        return userLocation;
    }

    @PostMapping("/app/loadMap")
    public String loadMap(@RequestBody Object obj){
        // TODO: load and return map
        return "";
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
    public boolean login(HttpSession session, @RequestBody User user){
        if( userService.validateUser(user)){
             session.setAttribute("id", user.getId());
             return true;
        }
        return false;
    }

    @PostMapping("/app/logout")
    public boolean logout(HttpSession session, @RequestBody User user){
        session.setAttribute("id", null);
        return true;

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
