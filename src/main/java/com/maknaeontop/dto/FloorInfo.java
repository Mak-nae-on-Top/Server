package com.maknaeontop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

/**
 * FloorInfo dto class that contains information about the building
 * and a base64-encoded image of its floor.
 */
@Getter
@Setter
@AllArgsConstructor
public class FloorInfo {
    private String uuid;
    private String building_name;
    private String floor;
    private String base64;
    private List<HashMap<String, Object>> coordinate;
    private List<HashMap<String, Object>> beaconInfo;

}
