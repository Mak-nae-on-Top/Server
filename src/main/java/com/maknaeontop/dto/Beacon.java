package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Beacon {
    private String uuid;
    private String major;
    private String minor;
    private float x;
    private float y;
    private String floor;
    private float accuracy;
    private long id;

    public Beacon(String uuid, String major, String minor){
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
