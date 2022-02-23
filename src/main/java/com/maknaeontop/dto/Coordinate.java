package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Coordinate is a dto class used when communicating using coordinates.
 * It contains x,y coordinates and methods to convert those coordinates into strings.
 */
@Getter
@Setter
public class Coordinate {
    private float x;
    private float y;

    public String toStringCoordinate(){
        return (int) this.x + ":" + (int) this.y;
    }
}
