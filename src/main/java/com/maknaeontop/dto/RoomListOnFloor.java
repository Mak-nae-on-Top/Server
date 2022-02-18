package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomListOnFloor {
    private String uuid;
    private String floor;
    private List<Room> coordinates;
}
