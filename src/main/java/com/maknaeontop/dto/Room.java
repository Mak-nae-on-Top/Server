package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Room {
    private String uuid;
    private String roomName;
    private float x;
    private float y;
    private int floor;

    public Room(String roomName, float x, float y, int floor){
        this.roomName = roomName;
        this.x = x;
        this.y = y;
        this.floor = floor;
    }
}
