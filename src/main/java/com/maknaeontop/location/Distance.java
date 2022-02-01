package com.maknaeontop.location;

import com.maknaeontop.dto.Beacon;

import java.util.List;

public class Distance {
    private float PATH_LOSS_PARAMETER = 2.5f;

    public static Distance getInstance(){
        return Holder.instance;
    }

    /**
     * Calculate the distance with Friis formula
     * @param beacon
     * @return
     */
    public float calculateDistance(Beacon beacon){
        int rssi = beacon.getRssi();
        int txPower = beacon.getTxPower();
        int pathLoss = calculatePathLoss(rssi, txPower);

        return (float) Math.pow(10,pathLoss/10*PATH_LOSS_PARAMETER);
    }

    public void saveDistance(List<Beacon> beaconList){
        for(Beacon beacon : beaconList){
            beacon.setDistance(calculateDistance(beacon));
        }
    }

    /**
     * Calculate the path loss
     * @param rssi          the received RSSI from beacon
     * @param txPower       the received TX-Power from beacon
     */
    private int calculatePathLoss(int rssi, int txPower){
        return rssi - txPower;
    }

    // Private constructor
    private Distance(){}

    // LazyHolder for singleton pattern
    private static class Holder{
        private static final Distance instance = new Distance();
    }
}
