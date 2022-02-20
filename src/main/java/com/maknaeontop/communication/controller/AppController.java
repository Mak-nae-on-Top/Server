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
    private final RoomService roomService;
    private final BuildingService buildingService;
    private final TrilaterationModelService trilaterationModelService;
    private final Location location = Location.getInstance();
    private final Response response = new Response();
    private final BlueprintUtil blueprintUtil = new BlueprintUtil();
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

        String uuid = beaconList.get(0).getUuid();

        // 비콘들의 xy정보 가져와서 저장 및 uuid, floor 추출
        // 0번째 비콘과 같은 층의 비콘들의 좌표 3개만 가져옴. 3개 미만이면 null 반환
        List<Beacon> beaconListIncludeLocation = beaconService.loadBeaconLocation(uuid, beaconList);
        // 리스트 사이즈가 3보다 작으면 fail
        if(beaconListIncludeLocation == null) return response.statusResponse("fail", "beacon wasn't searched enough");
        int floor = Integer.parseInt(beaconListIncludeLocation.get(0).getFloor());

        // 사용자 위치 계산
        HashMap<String, Float> userLocation = location.calculateUserLocation(beaconListIncludeLocation);
        // 사용자 위치 저장 후 같은 층 사람들 위치 반환 (사용자 위치가 0번째)
        List<HashMap<String, Float>> locationList = populationService.selectLocationAfterInsert(deviceId, uuid, userLocation.get("x"), userLocation.get("y"), floor);

        Population population = new Population("success", uuid, floor);
        population.setLocation_list(locationList);

        return response.locationResponse(population);
    }

    @PostMapping(value = "/loadMap")
    public String loadMap(@RequestBody UuidAndFloor uuidAndFloor) throws IOException {
        String base64Image = blueprintUtil.loadImage(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
        return response.base64Response("success", base64Image);
    }

    @PostMapping("/loadRoute")
    public String loadRoute(@RequestBody RouteRequest routeRequest, HttpServletRequest request) throws IOException, InterruptedException {
        final String deviceId = request.getHeader("Device");

        // 사용자와 같은 건물,층에 있는 모든 사용자를 가져오되, 사용자가 0번째가 되도록
        List<HashMap<String, Float>> location = populationService.selectLocationInSameFloor(routeRequest.getUuid(), Integer.parseInt(routeRequest.getFloor()), deviceId);
        // 목적지 리스트 가져오기 - 사용자 uuid, floor, 목적지이름을 통해서
        List<HashMap<String, Object>> roomList = roomService.selectLocationByUuidAndFloorAndRoomName(routeRequest);

        return blueprintUtil.getRoute(location, roomList);
    }

    @PostMapping("/manager/saveMap")
    public String saveMap(@RequestBody Base64Image base64Image, HttpServletRequest request) throws IOException {
        String id = jwtTokenUtil.getIdFromToken(request);
        HashMap<String, Integer> floorRange = buildingService.selectFloorRangeByUuid(base64Image.getUuid());
        if(floorRange == null){ // 새로 등록하는 빌딩인 경우
            messageRepository.createRoom(base64Image.getUuid(), new WebSocketRoom());
        }else { // 기존에 존재하는 빌딩을 수정하는 경우
            if(floorRange.get("lowest_floor") > Integer.parseInt(base64Image.getFloor())){
                buildingService.updateLowestFloor(base64Image.getUuid(), base64Image.getFloor());
            }
            if(floorRange.get("highest_floor") < Integer.parseInt(base64Image.getFloor())){
                buildingService.updateHighestFloor(base64Image.getUuid(), base64Image.getFloor());
            }
        }

        if(!blueprintUtil.saveImage(base64Image)){
            return response.statusResponse("fail","fail to convert image to map");
        }
        buildingService.insertBuilding(base64Image, id);
        return response.statusResponse("success","image save success");
    }

    @PostMapping("/manager/modifyCoordinates")
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
        roomService.deleteByUuidAndFloor(uuidAndFloor);         // room table

        return response.statusResponse("sucess", "floor data is deleted");
    }

    @PostMapping("/manager/enterBeaconLocation")
    public String enterBeaconLocation(@RequestBody EnterBeaconRequest enterBeaconRequest){
        beaconService.addBeaconList(enterBeaconRequest);
        return response.statusResponse("success","saved beacon info");
    }

    @PostMapping("/manager/loadAllMap")
    public String loadAllMap(HttpServletRequest request) {
        String id = jwtTokenUtil.getIdFromToken(request);
        List<HashMap<String, Object>> buildingList = buildingService.selectByManager(id);   // 빌딩 리스트 가져오기
        List<FloorInfo> mapList = new ArrayList<>();

        for(HashMap<String, Object> buildingHashMap : buildingList){    // 빌딩 돌면서 uuid, 건물이름, 가장 낮은층, 가장 높은층 가져오기
            String uuid = buildingHashMap.get("uuid").toString();
            String buildingName = buildingHashMap.get("name").toString();
            int lowestFloor = (int) buildingHashMap.get("lowest_floor");
            int highestFloor = (int) buildingHashMap.get("highest_floor");

            for(int floor=lowestFloor; floor<=highestFloor;floor++){    // 건물 각 층 돌면서
                try {
                    List<HashMap<String, Object>> beaconInfo = beaconService.selectByUuidAndFloor(uuid, floor); // 비콘 리스트 가져오기
                    List<HashMap<String, Object>> coordinate = roomService.selectByUuidAndFloor(uuid, floor);   // 각 층별 방 정보(x, y, roomName, id) 가져오기
                    String base64Image = blueprintUtil.loadImage(uuid, Integer.toString(floor));
                    FloorInfo floorInfo = new FloorInfo(uuid, buildingName, Integer.toString(floor), base64Image, coordinate, beaconInfo); // 싹 다 저장
                    mapList.add(floorInfo);
                }catch (NullPointerException | IOException e){
                    continue;
                }
            }
        }
        return response.allMapResponse(mapList);
    }

    // a, b 계산을 위한 init, 앱에서 데이터 n번 모아서 보내줘야 함 
    @PostMapping("/manager/init")
    public void initBeacon(List<List<Beacon>> beaconList, float realX, float realY){
        String uuid = beaconList.get(0).get(0).getUuid();
        HashMap<String, Float> modelConstant = location.createModel(beaconList, realX, realY);
        trilaterationModelService.insertConstants(uuid, modelConstant.get("a"), modelConstant.get("b"));
    }
}
