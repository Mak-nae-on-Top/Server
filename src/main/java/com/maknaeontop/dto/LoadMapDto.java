package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoadMapDto {
    private String uuid;
    private int floor;
    private int width;
    private int height;
}