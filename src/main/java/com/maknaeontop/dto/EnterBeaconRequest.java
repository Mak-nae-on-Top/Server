package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnterBeaconRequest {
    private String uuid;
    private String floor;
    private List<Beacon> beaconInfo;
}
