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
    private final JwtTokenUtil jwtTokenUtil;

    /**
     *  Method to log in by receiving id and password
     *
     * @param user  the user information including id and password
     * @return      status and message in json format
     */
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

    /**
     * Method that receives id, password, and password2,
     * and sign up if password and password2 are the same and the ID does not already exist
     *
     * @param user  the user information including id, password, and password2
     * @return      status and message in json format
     */
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

    /**
     * Method to measure the user's location based on the beacon list obtained by the app
     *
     * @param beaconList    list of beacons obtained by the app, sorted by distance from the user
     * @param request       HttpServletRequest to get device id
     * @return              uuid, floor and the coordinates of the location of all users
     *                      on the same building and floor as user.
     *                      0th in the list is the user's location
     */
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

        // 사용자 위치 계산 with 모델
        HashMap<String, Float> constants = trilaterationModelService.selectConstants(uuid);
        HashMap<String, Float> userLocation;
        if(constants != null){
            userLocation = location.calculateUserLocationWithModel(constants.get("a"), constants.get("b"), beaconListIncludeLocation);
        }else {
            userLocation = location.calculateUserLocation(beaconListIncludeLocation);
        }
        // 사용자 위치 저장 후 같은 층 사람들 위치 반환 (사용자 위치가 0번째)
        List<HashMap<String, Float>> locationList = populationService.selectCoordinateAfterInsert(deviceId, uuid, userLocation.get("x"), userLocation.get("y"), floor);

        Population population = new Population("success", uuid, floor);
        population.setLocation_list(locationList);

        return response.locationResponse(population);
    }

    /**
     * Method that takes the uuid and floor of a building and returns a map of that location.
     *
     * @param uuidAndFloor  uuid and floor of user
     * @return              image encoded in base64
     * @throws IOException
     */
    @PostMapping(value = "/loadMap")
    public String loadMap(@RequestBody UuidAndFloor uuidAndFloor) throws IOException {
        String base64Image = blueprintUtil.loadImage(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
        return response.base64Response("success", base64Image);
    }

    /**
     * Method that returns an optimized route from the user's location to the destination.
     *
     * @param routeRequest  uuid and floor of user and destination that user wants to go
     * @param request       HttpServletRequest to get device id
     * @return              array of coordinates in the optimized route
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping("/loadRoute")
    public String loadRoute(@RequestBody RouteRequest routeRequest, HttpServletRequest request) throws IOException, InterruptedException {
        final String deviceId = request.getHeader("Device");

        // 사용자와 같은 건물,층에 있는 모든 사용자를 가져오되, 사용자가 0번째가 되도록
        List<Coordinate> location = populationService.selectCoordinateInSameFloor(routeRequest.getUuid(), Integer.parseInt(routeRequest.getFloor()), deviceId);
        // 목적지 리스트 가져오기 - 사용자 uuid, floor, 목적지이름을 통해서
        List<Coordinate> roomList = roomService.selectCoordinateByUuidAndFloorAndRoomName(routeRequest);

        // TODO: for문 말고 다른방법은 없을까?
        List<String> locationArray = new ArrayList<>();
        List<String> roomArray = new ArrayList<>();
        for(Coordinate coordinate:location){
            locationArray.add(coordinate.toStringCoordinate());
        }
        for(Coordinate coordinate:roomList){
            roomArray.add(coordinate.toStringCoordinate());
        }
        /*
        String coordinates = blueprintUtil.getRoute(locationArray, roomArray, routeRequest.getUuid(), routeRequest.getFloor());

        if(coordinates==null){
            return response.routeResponse("fail", "can't get route");
        }
        return response.routeResponse("success", coordinates);
        */
        return "{\"status\":\"success\",\"coordinates\":[ {\"x\": 96, \"y\": 192},{\"x\": 97,\"y\": 193}]}";
    }

    /**
     * Method to receive a base64-encoded blueprint image, convert it to a map,
     * and save it to the server.
     *
     * @param base64Image   base64-encoded blueprint image
     * @param request       HttpServletRequest to get id
     * @return              status and message in json format
     * @throws IOException
     */
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

    /**
     * Method to set the name and coordinates of a room on the corresponding floor of the building.
     *
     * @param roomListOnFloor   uuid, floor and room list contains coordinate and room name.
     * @return                  status and message in json format
     */
    @PostMapping("/manager/modifyCoordinates")
    public String enterRoomName(@RequestBody RoomListOnFloor roomListOnFloor){
        roomService.insertRoom(roomListOnFloor);

        return response.statusResponse("success", "saved successfully");
    }

    /**
     * Method to delete all information in the floor.
     *
     * @param uuidAndFloor  uuid and floor
     * @return              status and message in json format
     */
    @PostMapping("/manager/deleteFloor")
    public String deleteFloor(@RequestBody UuidAndFloor uuidAndFloor){
        // 이미지 찾아서 지우기
        blueprintUtil.deleteMap(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
        // 디비에서 지우기
        beaconService.deleteByUuidAndFloor(uuidAndFloor);       // beacon table
        buildingService.deleteFloor(uuidAndFloor);              // building table
        roomService.deleteByUuidAndFloor(uuidAndFloor);         // room table

        return response.statusResponse("success", "floor data is deleted");
    }

    /**
     * Method to register information including location of beacon.
     * @param enterBeaconRequest    uuid, floor and beacon's information list
     *                              including major, minor, coordinate
     * @return                      status and message in json format
     */
    @PostMapping("/manager/enterBeaconLocation")
    public String enterBeaconLocation(@RequestBody EnterBeaconRequest enterBeaconRequest){
        beaconService.addBeaconList(enterBeaconRequest);

        return response.statusResponse("success","saved beacon info");
    }

    /**
     * Method that returns all information including floor maps of all buildings
     * where the user id is registered as an manager.
     *
     * @param request   HttpServletRequest to get id
     * @return          all information of buildings
     */
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

    /**
     * Method to obtain a model for correcting position measurements using trilateration.
     *
     * @param coordinateAndRangedBeacons     list of beacons obtained by the app
     * @return                  status and message in json format
     */
    @PostMapping("/manager/init")
    public String initBeacon(@RequestBody InitRequest coordinateAndRangedBeacons){
        List<List<Beacon>> testList = new ArrayList<>();
        String uuid = coordinateAndRangedBeacons.getRangedBeacons().get(0).get(0).getUuid();

        for(int i=0;i<coordinateAndRangedBeacons.getRangedBeacons().size();i++){
            List<Beacon> beaconList = coordinateAndRangedBeacons.getRangedBeacons().get(i);
            List<Beacon> beaconListIncludeLocation = beaconService.loadBeaconLocation(beaconList.get(0).getUuid(), beaconList);
            if(beaconListIncludeLocation == null){
                continue;
            }
            testList.add(beaconListIncludeLocation);
        }
        HashMap<String, Float> modelConstant = location.createModel(coordinateAndRangedBeacons.getX(), coordinateAndRangedBeacons.getY(), testList);
        trilaterationModelService.insertConstants(uuid, modelConstant.get("a"), modelConstant.get("b"));

        return response.statusResponse("success", "initialized trilateration model successfully");
    }

    /**
     * Method to create websocket room
     * @param uuid  building uuid
     */
    @GetMapping("/admin/createWebsocketRoom")
    public String createWebsocketRoom(@RequestParam String uuid){
        messageRepository.createRoom(uuid, new WebSocketRoom());
        return response.statusResponse("success", "successfully created websocket room");
    }
}
