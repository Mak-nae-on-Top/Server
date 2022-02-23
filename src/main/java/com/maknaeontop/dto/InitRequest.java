package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * InitRequest is a class used when communicating to obtain a model,
 * and has coordinates and a beacon list.
 */
@Getter
@Setter
public class InitRequest {
    private float x;
    private float y;
    private List<List<Beacon>> rangedBeacons;
}
