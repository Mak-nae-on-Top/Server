package com.maknaeontop.communication.controller;

import com.maknaeontop.communication.sevice.*;
import com.maknaeontop.location.Location;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("fireAlarm")
public class FireAlarmController {
    private final UserService userService;
    private final BeaconService beaconService;
    private final PopulationService populationService;
    private RoomService roomService;
    private BuildingService buildingService;
    private final Location location = Location.getInstance();

    // Constructor
    public FireAlarmController(UserService userService, BeaconService beaconService, PopulationService populationService){
        this.userService = userService;
        this.beaconService = beaconService;
        this.populationService = populationService;
    }

    @GetMapping("")
    public String communicateWithFireAlarm(@RequestParam String message){
        // TODO: send message to application
        return message;
    }
}
