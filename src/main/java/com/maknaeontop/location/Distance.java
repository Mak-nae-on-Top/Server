package com.maknaeontop.location;

public class Distance {
    public Distance getInstance(){
        return Holder.instance;
    }

    /**
     * Calculate the distance with Friis formula
     * @param rssi                  the received RSSI from beacon
     * @param txPower               the received TX-Power from beacon
     * @param pathLossParameter     the Pre-defined path loss parameter
     */
    public float calculateDistance(int rssi, int txPower, float pathLossParameter){
        int pathLoss = calculatePathLoss(rssi, txPower);
        return (float) Math.pow(10,pathLoss/10*pathLossParameter);
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
