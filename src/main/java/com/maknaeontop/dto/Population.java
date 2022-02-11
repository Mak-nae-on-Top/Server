package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
public class Population {
    private String uuid;
    private int floor;
    List<HashMap<String, Float>> locationList;

    public Population(String uuid, int floor){
        this.uuid = uuid;
        this.floor = floor;
    }
}
