package com.maknaeontop;

public class Beacon implements Comparable<Beacon>{
    private String uuid;
    private String major;
    private String minor;
    private float x;
    private float y;
    private float z;
    private float distance;

    // rssi and txpower can not be used
    private int rssi;
    private int txPower;

    public Beacon(String uuid, String major, String minor, int rssi, int txPower){
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.rssi = rssi;
        this.txPower = txPower;
    }

    // Getter
    public String getUuid() {
        return uuid;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getDistance() {
        return distance;
    }

    public int getRssi() {
        return rssi;
    }

    public int getTxPower() {
        return txPower;
    }

    // Setter
    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setLocation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int compareTo(Beacon o) {
        return this.rssi - o.rssi;
    }
}
