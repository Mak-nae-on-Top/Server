package com.maknaeontop.location;

import com.maknaeontop.Beacon;

import java.util.Collections;
import java.util.List;

public class Location {
    private Distance distance = Distance.getInstance();
    private Trilateration trilateration = Trilateration.getInstance();

    public static Location getInstance() {
        return Holder.instance;
    }

    public float[] findUserLocation(List<Beacon> beaconList){
        distance.saveDistance(beaconList);
        Collections.sort(beaconList);
        return trilateration.calculateTrilateration(beaconList.get(0), beaconList.get(1), beaconList.get(2));
    }

    private Location(){}

    // LazyHolder
    private static class Holder{
        private static final Location instance = new Location();
    }

}
