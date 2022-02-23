package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * EnterBeaconRequest is a dto class that includes beacon information, uuid, and floor information.
 */
@Getter
@Setter
public class EnterBeaconRequest {
    private String uuid;
    private String floor;
    private List<Beacon> beaconInfo;
}
