package com.maknaeontop.communication.sevice;

import com.maknaeontop.communication.mapper.BeaconMapper;
import com.maknaeontop.communication.mapper.UserMapper;
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

    public HashMap<String, Object> getLocation(String uuid, byte[] major, byte[] minor){
        return beaconMapper.getLocation(uuid, major, minor);
    }

    public List<HashMap<String, Object>> addBeacon(String uuid, byte[] major, byte[] minor, float x, float y, float z){
        return beaconMapper.addBeacon(uuid, major, minor, x, y, z);
    }
}
