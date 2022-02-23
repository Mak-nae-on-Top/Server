package com.maknaeontop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * RouteRequest is a dto class used when requesting a route from the app to the server,
 * and includes uuid, floor, and the name of the destination location.
 */
@Setter
@Getter
public class RouteRequest {
    private String uuid;
    private String floor;
    private String destination;
}
