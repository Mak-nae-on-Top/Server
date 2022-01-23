package com.maknaeontop;

public class Beacon implements Comparable<Beacon>{
    private String uuid;
    private int rssi;
    private byte[] major;
    private byte[] minor;
    private int txPower;
    private float distance;

    public Beacon(String uuid, int rssi, byte[] major, byte[] minor, int txPower){
        this.uuid = uuid;
        this.rssi = rssi;
        this.major = major;
        this.minor = minor;
        this.txPower = txPower;
    }

    public String getUuid() {
        return uuid;
    }

    public int getRssi() {
        return rssi;
    }

    public byte[] getMajor() {
        return major;
    }

    public byte[] getMinor() {
        return minor;
    }

    public int getTxPower() {
        return txPower;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Beacon o) {
        return this.rssi - o.rssi;
    }
}
