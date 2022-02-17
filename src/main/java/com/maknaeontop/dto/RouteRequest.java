package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RouteRequest {
    private String uuid;
    private String floor;
    private String destination;
}
