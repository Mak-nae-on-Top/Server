package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.BeaconMapper;
import com.maknaeontop.dto.Beacon;
import com.maknaeontop.dto.EnterBeaconRequest;
import com.maknaeontop.dto.UuidAndFloor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BeaconService {
    private static BeaconMapper beaconMapper;

    public BeaconService(BeaconMapper beaconMapper){
        this.beaconMapper = beaconMapper;
    }

    public HashMap<String, Object> selectCoordinate(String uuid, String major, String minor){
        return beaconMapper.selectCoordinate(uuid, major, minor);
    }

    public void addBeacon(long id, String uuid, String major, String minor, float x, float y, int floor){
        beaconMapper.addBeacon(id, uuid, major, minor, x, y, floor);
    }

    public void addBeaconList(EnterBeaconRequest enterBeaconRequest){
        String uuid = enterBeaconRequest.getUuid();
        int floor = Integer.parseInt(enterBeaconRequest.getFloor());
        for(Beacon beacon : enterBeaconRequest.getBeaconInfo()){
            addBeacon(beacon.getId(), uuid, beacon.getMajor(), beacon.getMinor(), beacon.getX(), beacon.getY(), floor);
        }
    }

    public List<Beacon> loadBeaconLocation(String uuid, List<Beacon> beaconList){
        int count = 0;
        for(Beacon beacon : beaconList){
            HashMap<String, Object> location = selectCoordinate(uuid, beacon.getMajor(), beacon.getMinor());
            if(location != null){
                beacon.setFloor(Integer.toString((int)location.get("floor")));
                beacon.setLocation((float)location.get("x"), (float)location.get("y"));
            }
            if(++count == 3) return beaconList;
        }
        return null;
    }

    public void deleteByUuidAndFloor(UuidAndFloor uuidAndFloor){
        beaconMapper.deleteByUuidAndFloor(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
    }

    public List<HashMap<String, Object>> selectByUuidAndFloor(String uuid, int floor){
        return beaconMapper.selectByUuidAndFloor(uuid, floor);
    }
}
