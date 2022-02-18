package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Base64Image {
    private String uuid;
    private String floor;
    private String base64;
    private String building_name;
    private int image_width;
    private int image_height;
}
