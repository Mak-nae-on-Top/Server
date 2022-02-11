package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Beacon implements Comparable<Beacon>{
    private String uuid;
    private String major;
    private String minor;
    private float x;
    private float y;
    private int floor;
    private float accuracy;

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

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Beacon o) {
        return this.rssi - o.rssi;
    }
}
