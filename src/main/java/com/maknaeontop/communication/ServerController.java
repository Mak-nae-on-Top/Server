package com.maknaeontop.communication;

import com.maknaeontop.Beacon;
import com.maknaeontop.communication.sevice.UserService;
import com.maknaeontop.location.Location;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class ServerController {
    private UserService userService;
    private Location location;

    public ServerController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/test")
    public List<HashMap<String, Object>> getUsers(@RequestBody Beacon[] beaconList){
        String userLocation = location.findLocation();
        return userService.getUsers();
    }
}
