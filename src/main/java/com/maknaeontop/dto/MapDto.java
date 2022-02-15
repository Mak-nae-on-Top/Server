package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapDto {
    private String uuid;
    private String buildingName;
    private String floor;
    private String base64;

    public MapDto(String uuid, String buildingName, String floor, String base64){
        this.uuid = uuid;
        this.buildingName = buildingName;
        this.floor = floor;
        this.base64 = base64;
    }
}
