package com.maknaeontop.communication.controller;

import com.maknaeontop.communication.JsonBuilder;
import com.maknaeontop.dto.Beacon;
import com.maknaeontop.dto.Device;
import com.maknaeontop.dto.Room;
import com.maknaeontop.dto.User;
import com.maknaeontop.communication.sevice.*;
import com.maknaeontop.location.Location;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("app")
public class AppController {
    private final String PATHPREFIX = "~/Image/blueprint";
    private final String EXTENSION = ".jpg";

    private final UserService userService;
    private final BeaconService beaconService;
    private final PopulationService populationService;
    private RoomService roomService;
    private BuildingService buildingService;
    private final Location location = Location.getInstance();
    private final JsonBuilder jsonBuilder = new JsonBuilder();

    // Constructor
    public AppController(UserService userService, BeaconService beaconService, PopulationService populationService){
        this.userService = userService;
        this.beaconService = beaconService;
        this.populationService = populationService;
    }

    @PostMapping("/location")
    public String estimateLocation(@RequestBody List<Beacon> beaconList, HttpSession session){
        String id = String.valueOf(session.getAttribute("device"));
        String uuid = beaconList.get(0).getUuid();

        // load location using UUID, major and minor
        for(Beacon beacon : beaconList){
            HashMap<String, Object> location = beaconService.getLocation(beacon.getUuid(), beacon.getMajor(), beacon.getMinor());
            beacon.setLocation((float)location.get("x"), (float)location.get("y"));
        }
        // find user location
        float[] userLocation = location.findUserLocation(beaconList);

        // save user location on DB
        populationService.insertUserLocation(id, uuid, userLocation[0], userLocation[1], 0f/* userLocation[2]*/);

        // return the user location list of the building
        return jsonBuilder.locationResponse(userLocation);
    }

    @PostMapping(value = "/loadMap", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> loadMap(@RequestParam("uuid") String uuid, @RequestParam("floor") String floor){
        try{
            Path path = Paths.get(PATHPREFIX + uuid + "_" + floor + EXTENSION);
            String contentType = Files.probeContentType(path);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("filename", StandardCharsets.UTF_8).build());
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            Resource resource = new InputStreamResource(Files.newInputStream(path));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }catch (Exception e){
            // TODO: handle exception
            return null;
        }
    }

    @PostMapping("/saveMap")
    public String saveMap(@RequestPart MultipartFile multipartFile, @RequestParam("uuid") String uuid, @RequestParam("floor") String floor){
        try{
            File newFileName = new File(PATHPREFIX + uuid + "_" + floor + EXTENSION);
            multipartFile.transferTo(newFileName);
        }catch (Exception e){
            return jsonBuilder.statusResponse("fail","try again");
        }
        return jsonBuilder.statusResponse("success","image save success");
    }

    @PostMapping("/enterRoomName")
    public String enterRoomName(Room room){

        roomService.insertRoom(room);
        return "false";
    }

    @PostMapping("/enterBeaconLocation")
    public String enterBeaconLocation(@RequestBody List<Beacon> beaconList){
        try{
            for(Beacon beacon : beaconList){
                beaconService.addBeacon(beacon.getUuid(), beacon.getMajor(), beacon.getMinor(), beacon.getX(), beacon.getY(), beacon.getFloor());
            }
            return jsonBuilder.statusResponse("success","");
        }catch (Exception e){
            return jsonBuilder.statusResponse("fail","try again");
        }
    }

    @PostMapping("/login")
    public String login(HttpSession session, @RequestBody User user){
        String pwInDatabase = userService.selectPwUsingId(user.getId());
        if(pwInDatabase != null){
            if(pwInDatabase.equals(user.getPassword())){
                return jsonBuilder.tokenResponse("success","login complete","temp_token");
            }
            return jsonBuilder.tokenResponse("fail","password does not match","");
        }
        return jsonBuilder.tokenResponse("fail","id does not exist","");
    }

    @PostMapping("/start")
    public void start(HttpSession session, @RequestBody Device device){
        session.setAttribute("device", device);
    }

    @PostMapping("/test")
    public String test(HttpSession session){
        return session.getAttribute("device").toString();
    }

    @PostMapping("/logout")
    public boolean logout(HttpSession session, @RequestBody User user){
        session.setAttribute("id", null);
        return true;
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

    @PostMapping("/event")
    private String event(String msg){
        // TODO: send message to application
        return msg;
    }
}
