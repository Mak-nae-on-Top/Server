package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.BeaconMapper;
import com.maknaeontop.dto.Beacon;
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

    public List<HashMap<String, Object>> selectAllBeacons(){
        return beaconMapper.selectAllBeacons();
    }

    public List<HashMap<String, Object>> selectBeaconsUsingUUID(String uuid){
        return beaconMapper.selectBeaconsUsingUUID(uuid);
    }

    public HashMap<String, Object> selectLocation(String uuid, String major, String minor){
        return beaconMapper.selectLocation(uuid, major, minor);
    }

    public void addBeacon(String uuid, String major, String minor, float x, float y, int floor){
        beaconMapper.addBeacon(uuid, major, minor, x, y, floor);
    }

    public void addBeaconList(List<Beacon> beaconList){
        for(Beacon beacon : beaconList){
            addBeacon(beacon.getUuid(), beacon.getMajor(), beacon.getMinor(), beacon.getX(), beacon.getY(), beacon.getFloor());
        }
    }

    public int selectFloor(Beacon beacon){
        return beaconMapper.selectFloor(beacon.getUuid(), beacon.getMajor(), beacon.getMinor());
    }

    public List<Beacon> loadBeaconLocation(List<Beacon> beaconList){
        for(Beacon beacon : beaconList){
            HashMap<String, Object> location = selectLocation(beacon.getUuid(), beacon.getMajor(), beacon.getMinor());
            beacon.setFloor((int)location.get("floor"));
            beacon.setLocation((float)location.get("x"), (float)location.get("y"));
        }
        return beaconList;
    }

    public HashMap<String, Float> selectMaxXYByUuidAndFloor(String uuid, int floor){
        return beaconMapper.selectMaxXYByUuidAndFloor(uuid, floor);
    }

    public void deleteByUuidAndFloor(UuidAndFloor uuidAndFloor){
        beaconMapper.deleteByUuidAndFloor(uuidAndFloor.getUuid(), uuidAndFloor.getFloor());
    }
}
