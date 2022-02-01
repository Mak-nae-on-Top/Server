package com.maknaeontop.dto;

public class Room {
    private String roomName;
    private float x;
    private float y;
    private float z;

    public Room(String roomName, float x, float y, float z){
        this.roomName = roomName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getRoomName() {
        return roomName;
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
}
