package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Base64Image is a dto class that stores base64-encoded images
 * and the corresponding building and floor information.
 */
@Getter
@Setter
public class Base64Image {
    private String uuid;
    private String floor;
    private String base64;
    private String building_name;
}
