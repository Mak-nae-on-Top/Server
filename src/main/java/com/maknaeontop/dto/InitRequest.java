package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InitRequest {
    private float x;
    private float y;
    private List<List<Beacon>> rangedBeacons;
}
