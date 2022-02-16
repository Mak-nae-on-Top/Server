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
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
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
    public String loadMap(@RequestBody UuidAndFloor uuidAndFloor) throws IOException {
        String base64Image = blueprintUtil.loadImage(uuidAndFloor.getUuid(), Integer.parseInt(uuidAndFloor.getFloor()));
        HashMap<String, Integer> heightAndWidth = floorService.selectHeightsAndWidthsByFloor(uuidAndFloor.getUuid(), Integer.parseInt(uuidAndFloor.getFloor()));
        return response.base64Response("success", heightAndWidth, base64Image);
    }

    @PostMapping("/manager/saveMap")
    public String saveMap(@RequestBody Base64Image base64Image) throws IOException {
        HashMap<String, Integer> floorRange = buildingService.selectFloorRangeByUuid(base64Image.getUuid());
        if(floorRange.get("lowest_floor") > Integer.parseInt(base64Image.getFloor())){
            buildingService.updateLowestFloor(base64Image.getUuid(), base64Image.getFloor());
        }
        if(floorRange.get("highest_floor") < Integer.parseInt(base64Image.getFloor())){
            buildingService.updateHighestFloor(base64Image.getUuid(), base64Image.getFloor());
        }
        if(!blueprintUtil.saveImage(base64Image)){
            return response.statusResponse("fail","fail to convert image to map");
        }
        floorService.insertImageInfo(base64Image.getUuid(), Integer.parseInt(base64Image.getFloor()), base64Image.getImageHeight(), base64Image.getImageWidth());
        return response.statusResponse("success","image save success");
    }

    @PostMapping("/manager/enterRoomName")
    public String enterRoomName(@RequestBody RoomListOnFloor roomListOnFloor){
        roomService.insertRoom(roomListOnFloor);
        return response.statusResponse("success", "saved successfully");
    }

    @PostMapping("/manager/deleteFloor")
    public String deleteFloor(@RequestBody UuidAndFloor uuidAndFloor){
        // 이미지 찾아서 지우기
        blueprintUtil.deleteMap(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
        // 디비에서 지우기
        beaconService.deleteByUuidAndFloor(uuidAndFloor);       // beacon table
        buildingService.deleteFloor(uuidAndFloor);              // building table
        floorService.deleteFloorByUuidAndFloor(uuidAndFloor);   // floor table
        roomService.deleteByUuidAndFloor(uuidAndFloor);         // room table

        return response.statusResponse("sucess", "floor data is deleted");
    }

    @PostMapping("/manager/enterBeaconLocation")
    public String enterBeaconLocation(@RequestBody List<Beacon> beaconList){
        beaconService.addBeaconList(beaconList);
        return response.statusResponse("success","saved beacon info");
    }

    @PostMapping("/manager/loadAllMap")
    public List<MapDto> loadAllMap(HttpServletRequest request) throws IOException {
        String id = jwtTokenUtil.getIdFromToken(request);
        List<HashMap<String, Object>> buildingList = buildingService.selectByManager(id);   // 빌딩 리스트 가져오기
        List<MapDto> mapList = new ArrayList<>();

        for(HashMap<String, Object> buildingHashMap : buildingList){    // 빌딩 돌면서 uuid, 건물이름, 가장 낮은층, 가장 높은층 가져오기
            String uuid = buildingHashMap.get("uuid").toString();
            String buildingName = buildingHashMap.get("name").toString();
            int lowestFloor = (int) buildingHashMap.get("lowest_floor");
            int highestFloor = (int) buildingHashMap.get("highest_floor");

            for(int floor=lowestFloor; floor<=highestFloor;floor++){    // 건물 각 층 돌면서
                try {
                    HashMap<String, Integer> hw = floorService.selectHeightsAndWidthsByFloor(uuid, floor);  // width, height 가져오기
                    List<HashMap<String, Object>> roomInfo = roomService.selectByUuidNFloor(uuid, floor);   // 각 층별 방 정보(x, y, roomName) 가져오기
                    String base64Image = blueprintUtil.loadImage(uuid, floor);
                    MapDto mapDto = new MapDto(uuid, buildingName, Integer.toString(floor), base64Image, hw.get("image_width"), hw.get("image_height"), hw.get("blueprint_width"), hw.get("blueprint_height"), roomInfo); // 싹 다 저장
                    mapList.add(mapDto);
                }catch (NullPointerException e){
                    continue;
                }
            }
        }

        return mapList;
    }

    @GetMapping("/createWebsocketRoom")
    public void createRoom(@RequestParam String uuid){
        WebSocketRoom webSocketRoom = new WebSocketRoom();
        messageRepository.createRoom(uuid, webSocketRoom);
    }
}
