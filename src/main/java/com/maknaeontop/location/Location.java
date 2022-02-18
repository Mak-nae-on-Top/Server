package com.maknaeontop.location;

import com.maknaeontop.dto.Beacon;

import java.util.HashMap;
import java.util.List;

public class Location {
    private Distance distance = Distance.getInstance();
    private Trilateration trilateration = Trilateration.getInstance();

    public static Location getInstance() {
        return Holder.instance;
    }

    public HashMap<String, Float> calculateUserLocation(List<Beacon> beaconList){
        return trilateration.calculateTrilateration(beaconList.get(0), beaconList.get(1), beaconList.get(2));
    }

    private Location(){}

    // LazyHolder
    private static class Holder{
        private static final Location instance = new Location();
    }

}
