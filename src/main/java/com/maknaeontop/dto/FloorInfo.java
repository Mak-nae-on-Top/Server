package com.maknaeontop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FloorInfo {
    private String uuid;
    private String buildingName;
    private String floor;
    private String base64;
    private int image_width, image_height, blueprint_width, blueprint_height;
    private List<HashMap<String, Object>> coordinate;

}
