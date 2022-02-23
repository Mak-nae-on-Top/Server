package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Room is a dto class used for communication using room information located on the map.
 */
@Setter
@Getter
public class Room {
    private String room_name;
    private float x;
    private float y;
    private long id;
}
