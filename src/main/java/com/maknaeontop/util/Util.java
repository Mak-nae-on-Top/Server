package com.maknaeontop.util;

import com.maknaeontop.Beacon;

import java.util.Collections;
import java.util.List;

public class Util {

    public List<Beacon> getStrongest3Beacons(List<Beacon> beaconList){
        if(beaconList.isEmpty()) {
            return null;
        }

        Collections.sort(beaconList);
        if(beaconList.size() < 3) {
            return beaconList;
        }
        else return beaconList.subList(0,3);
    }


}
