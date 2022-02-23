package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * RoomListOnFloor is a class
 * including a list of room information on one floor, building information, and uuid.
 */
@Getter
@Setter
public class RoomListOnFloor {
    private String uuid;
    private String floor;
    private List<Room> coordinates;
}
