package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
public class Population {
    private String status;
    private String uuid;
    private int floor;
    List<HashMap<String, Float>> location_list;

    public Population(String status, String uuid, int floor){
        this.status = status;
        this.uuid = uuid;
        this.floor = floor;
    }
}
