package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coordinate {
    private float x;
    private float y;

    public String toStringCoordinate(){
        return (int) this.x + ":" + (int) this.y;
    }
}